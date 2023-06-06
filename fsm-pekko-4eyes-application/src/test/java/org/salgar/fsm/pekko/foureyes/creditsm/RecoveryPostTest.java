package org.salgar.fsm.pekko.foureyes.creditsm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.credit.model.Address;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.facade.CreditScoreSMFacade;
import org.salgar.fsm.pekko.foureyes.elasticsearch.CreditSMRepository;
import org.salgar.fsm.pekko.foureyes.model.CreditSmEs;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.CREDIT_ANALYST_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.RELATIONSHIP_MANAGER_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.SALES_MANAGER_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.SENIOR_SALES_MANAGER_NOTIFICATION_LIST;

/**
 * These tests are disabled because they are here to demonstrate the Recovery Capabilities of Pekko Framework, so
 * they are designed to run only once per day, if the continuous integration system would try to run multiple times
 * per day, they will fail.
 */
@Disabled
@EnableElasticsearchRepositories("org.salgar.fsm.pekko.foureyes.elasticsearch")
@ActiveProfiles({"itest"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        topics = {"creditSM", "creditScoreSM", "addressCheckSM", "fraudPreventionSM", "multiTenantScreditScoreSM"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecoveryPostTest {
    final List<String> relationShipNotificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
    final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");
    final List<String> creditAnalystNotificationList = List.of("creditanalyst@example.com");
    final List<String> seniorSalesManagerNotificationList = List.of("seniorSalesManagert@example.com");
    private final static long WAIT_TIME_BETWEEN_STEPS = TimeUnit.MILLISECONDS.toMillis(2000);
    private final static long WAIT_TIME_ELASTIC = TimeUnit.SECONDS.toMillis(10);

    @Autowired
    private CreditSMFacade creditSMFacade;

    @Autowired
    private CreditSMRepository creditSMRepository;

    @MockBean
    private NotifierService notifierService;

    @Autowired
    private CreditScoreSMFacade creditScoreSMFacade;

    @BeforeEach
    private void before() {
        prepareNotificationService();
    }

    @SneakyThrows
    @Test
    public void recoveryTest() {
        final String creditUuid = "0e679b7f-806f-41db-bd8e-21f3f62aa3e3_"
                + DateTimeFormatter.ofPattern("dd_MM_yyyy").format(LocalDate.now());
        final Customer customer1 =
                new Customer(
                        "John",
                        "Doe",
                        "123456789X",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "customer1@test.info");
        final Customer customer2 =
                new Customer(
                        "Max",
                        "Musterman",
                        "Z987654321",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "customer2@test.info");

        List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer1);
        creditTenants.add(customer2);

        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        /* Test Start */
        Future<CreditSM.Response> futureCreditSMState = creditSMFacade.currentState(payload);
        CreditSM.ReportResponse report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(),
                instanceOf(
                        CreditSM.CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED
                        .class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        for (Customer customer : creditTenants) {
            log.info("Sending Credit Score Result for Customer: {}", customer);

            Map<String, Object> creditScorePayload = new HashMap<>();
            creditScorePayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 97.45);
            creditScoreSMFacade.resultReceived(
                    () -> creditUuid + "_" + customer.getPersonalId(),
                    creditScorePayload);
        }

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);
        assertNotNull(creditSmEs);

        assertEquals(
                "WAITING_CREDIT_ANALYST_APPROVAL",
                creditSmEs.get().getState()
        );

        verify(notifierService, atLeastOnce()).notify(eq(creditAnalystNotificationList), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @SneakyThrows
    @Test
    public void recoveryOnlyOneCreditScoreEventReceivedTest() {
        final String creditUuid = "0e679b7f-806f-41db-bd8e-21f3f62aa3e4_"
                + DateTimeFormatter.ofPattern("dd_MM_yyyy").format(LocalDate.now());
        final Customer customer1 =
                new Customer(
                        "John",
                        "Doe",
                        "123456789X",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "customer1@test.info");
        final Customer customer2 =
                new Customer(
                        "Max",
                        "Musterman",
                        "Z987654321",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "customer2@test.info");

        List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer1);
        creditTenants.add(customer2);

        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        /* Test Start */
        Future<CreditSM.Response> futureCreditSMState = creditSMFacade.currentState(payload);
        CreditSM.ReportResponse report =
                (CreditSM.ReportResponse) Await
                        .result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(),
            instanceOf(
                CreditSM.CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_FRAUDPREVENTION_ADRESSCHECK_RESULT_RECEIVED.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Customer customer =  creditTenants.get(0);
        log.info("Sending Credit Score Result for Customer: {}", customer);

        Map<String, Object> creditScorePayload = new HashMap<>();
        creditScorePayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 83.45);
        creditScoreSMFacade.resultReceived(
                () -> creditUuid + "_" + customer.getPersonalId(),
                creditScorePayload);


        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);
        assertNotNull(creditSmEs);
        assertEquals(
                "WAITING_CREDIT_ANALYST_APPROVAL",
                creditSmEs.get().getState()
        );

        verify(notifierService, atLeastOnce()).notify(eq(creditAnalystNotificationList), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    private Map<String, Object> preparePayload (
            String creditUuid,
            List<Customer> creditTenants) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        //payload.put(PayloadVariableConstants.CREDIT_TENANTS, creditTenants);

        return payload;
    }

    private void prepareNotificationService() {
        doAnswer(
                invocation -> {
                    if(RELATIONSHIP_MANAGER_NOTIFICATION_LIST.equals(invocation.getArgument(0))) {
                        return relationShipNotificationList;
                    } else if(SALES_MANAGER_NOTIFICATION_LIST.equals(invocation.getArgument(0))) {
                        return salesManagerNotificationList;
                    } else if(CREDIT_ANALYST_NOTIFICATION_LIST.equals(invocation.getArgument(0))) {
                        return creditAnalystNotificationList;
                    } else if(SENIOR_SALES_MANAGER_NOTIFICATION_LIST.equals(invocation.getArgument(0))) {
                        return seniorSalesManagerNotificationList;
                    }
                    return null;
                }
        ).when(notifierService).calculateRecipientList(anyString());
    }
}