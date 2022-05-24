package org.salgar.fsm.akka.foureyes.creditsm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salgar.akka.fsm.foureyes.addresscheck.AddressCheckService;
import org.salgar.akka.fsm.foureyes.creditscore.CreditScoreService;
import org.salgar.akka.fsm.foureyes.faudprevention.FraudPreventionService;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.addresscheck.facade.AdressCheckSMFacade;
import org.salgar.fsm.akka.foureyes.credit.CreditSM;
import org.salgar.fsm.akka.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.akka.foureyes.credit.model.Address;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.creditscore.facade.CreditScoreSMFacade;
import org.salgar.fsm.akka.foureyes.elasticsearch.CreditSMRepository;
import org.salgar.fsm.akka.foureyes.elasticsearch.model.CreditSmEs;
import org.salgar.fsm.akka.foureyes.fraudprevention.facade.FraudPreventionSMFacade;
import org.salgar.fsm.akka.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.*;

@Disabled
@EnableElasticsearchRepositories("org.salgar.fsm.akka.foureyes.elasticsearch")
@ActiveProfiles({"itest"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        topics = {"creditSM", "creditScoreSM", "addressCheckSM", "fraudPreventionSM", "multiTenantScreditScoreSM"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class SlaveStateMachineTest {
    final List<String> relationShipNotificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
    final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");
    final List<String> creditAnalystNotificationList = List.of("creditanalyst@example.com");
    final List<String> seniorSalesManagerNotificationList = List.of("seniorSalesManagert@example.com");
    private final static long WAIT_TIME_BETWEEN_STEPS = TimeUnit.MILLISECONDS.toMillis(500);
    private final static long WAIT_TIME_ELASTIC = TimeUnit.SECONDS.toMillis(30);

    @Autowired
    private CreditSMFacade creditSMFacade;

    @Autowired
    private CreditSMRepository creditSMRepository;

    @MockBean
    private CreditScoreService creditScoreServiceMockBean;

    @MockBean
    private FraudPreventionService fraudPreventionServiceMockBean;

    @MockBean
    private AddressCheckService addressCheckServiceMockBean;

    @MockBean
    private NotifierService notifierService;

    @Autowired
    private CreditScoreSMFacade creditScoreSMFacade;

    @Autowired
    private FraudPreventionSMFacade fraudPreventionSMFacade;

    @Autowired
    private AdressCheckSMFacade addressCheckSMFacade;

    @BeforeEach
    private void before() {
        prepareNotificationService();
    }

    @SneakyThrows
    @Test
    public void slaveStateMachineTest() {
        final String creditUuid = UUID.randomUUID().toString();
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
                        "customer1@test.info");

        List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer1);
        creditTenants.add(customer2);

        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        /* Mock Preparation */

        doAnswer(invocation -> {
            if(invocation.getArgument(2).equals(customer1.getPersonalId())) {
                log.info("Sending Credit Score Result customer1");

                Thread.sleep(5000L);

                Map<String, Object> creditScorePayload = new HashMap<>();
                creditScorePayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 84.21);
                creditScoreSMFacade.resultReceived(
                        () -> creditUuid + "_" + customer1.getPersonalId(),
                        creditScorePayload);
            } else if(invocation.getArgument(2).equals(customer2.getPersonalId())) {
                log.info("Sending Credit Score Result customer2");

                Thread.sleep(5000L);

                Map<String, Object> creditScorePayload = new HashMap<>();
                creditScorePayload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 97.45);
                creditScoreSMFacade.resultReceived(
                        () -> creditUuid + "_" + customer2.getPersonalId(),
                        creditScorePayload);
            } else {
                String personalId = invocation.getArgument(2);
                log.warn("Unkown customer: {}", personalId);
            }

            return null;
        }).when(creditScoreServiceMockBean).calculateCreditScore(
                any(),
                any(),
                any());

        doAnswer(invocation -> {
            log.info("Sending Fraud Prevention Result");

            Thread.sleep(1000L);

            Map<String, Object> fraudPreventionResultPayload = new HashMap<>();
            fraudPreventionResultPayload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
            fraudPreventionSMFacade.result(
                    () -> creditUuid,
                    fraudPreventionResultPayload);

            return null;
        }).when(fraudPreventionServiceMockBean).reportFraudPrevention(
                any(),
                any(),
                any());

        doAnswer(invocation -> {
            log.info("Sending Address Check Result");

            Thread.sleep(2000L);

            Map<String, Object> addressCheckResultPayload = new HashMap<>();
            addressCheckResultPayload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
            addressCheckSMFacade.result(
                    () -> creditUuid,
                    addressCheckResultPayload);

            return null;
        }).when(addressCheckServiceMockBean).addressExist(
                any(),
                any(),
                any(),
                any());


        /* Test Start */
        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<CreditSM.Response> futureCreditSMState = creditSMFacade.currentState(payload);
        CreditSM.ReportResponse report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer1);
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(1), customer2);
        verify(notifierService, atLeastOnce()).notify(eq(relationShipNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CreditSM.CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL.class));

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(30000L);
        verify(notifierService, atLeastOnce()).notify(eq(salesManagerNotificationList), anyString());

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(
                CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL
                        .class
                        .getSimpleName()
                        .substring(
                                CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL
                                .class
                                .getSimpleName()
                                        .indexOf("_$_") + 3,
                                CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL
                                        .class
                                        .getSimpleName().length()),
                creditSmEs.get().getState());

        verify(creditScoreServiceMockBean, times(2)).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, atLeastOnce()).notify(eq(salesManagerNotificationList), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    private Map<String, Object> preparePayload (
            String creditUuid,
            List<Customer> creditTenants) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        payload.put(PayloadVariableConstants.CREDIT_TENANTS, creditTenants);

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