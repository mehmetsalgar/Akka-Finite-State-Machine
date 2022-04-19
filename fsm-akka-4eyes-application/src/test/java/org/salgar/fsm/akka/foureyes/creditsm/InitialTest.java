package org.salgar.fsm.akka.foureyes.creditsm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salgar.akka.fsm.foureyes.addresscheck.AddressCheckService;
import org.salgar.akka.fsm.foureyes.cra.kafka.CustomerRelationshipAdapter;
import org.salgar.akka.fsm.foureyes.cra.model.CRMCustomer;
import org.salgar.akka.fsm.foureyes.creditscore.CreditScoreService;
import org.salgar.akka.fsm.foureyes.faudprevention.FraudPreventionService;
import org.salgar.akka.fsm.foureyes.notifier.NotifierService;
import org.salgar.fsm.akka.foureyes.FSMAkka4EyesApplication;
import org.salgar.fsm.akka.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.akka.foureyes.credit.model.Address;
import org.salgar.fsm.akka.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.elasticsearch.CreditSMRepository;
import org.salgar.fsm.akka.foureyes.elasticsearch.model.CreditSmEs;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.salgar.akka.fsm.foureyes.notifier.NotificationHelper.*;
import static org.salgar.fsm.akka.foureyes.credit.CreditSM.*;
import static org.salgar.fsm.akka.foureyes.slaves.SlaveStatemachineConstants.*;


@EnableElasticsearchRepositories("org.salgar.fsm.akka.foureyes.elasticsearch")
@ActiveProfiles({"itest"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        topics = {"creditSM", "creditScoreSM", "addressCheckSM", "fraudPreventionSM", "multiTenantScreditScoreSM"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Slf4j
@SpringBootTest(classes = { FSMAkka4EyesApplication.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InitialTest {
    private final static long WAIT_TIME_BETWEEN_STEPS = TimeUnit.MILLISECONDS.toMillis(500);
    private final static long WAIT_TIME_ELASTIC = TimeUnit.SECONDS.toMillis(15);
    final List<String> relationShipNotificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
    final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");
    final List<String> creditAnalystNotificationList = List.of("creditanalyst@example.com");
    final List<String> seniorSalesManagerNotificationList = List.of("seniorSalesManagert@example.com");

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

    @MockBean
    private CustomerRelationshipAdapter customerRelationshipAdapter;

    @BeforeEach
    private void before() {
        prepareNotificationService();
    }

    @Test
    @SneakyThrows
    public void creditAcceptedTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(relationShipNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 73.72));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map = (Map<String, CreditTenantScoreResult>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(73.72, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.ADDRESS_CHECK_RESULT));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.accepted(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_ACCEPTED.class.getSimpleName());
        verify(notifierService, times(1)).notify(eq(List.of(customer.getEmail())), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());
        verify(customerRelationshipAdapter).transferCustomerCreation(
                eq(
                        new CRMCustomer(
                                customer.getFirstName(),
                                customer.getLastName()
                        )));

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void eventUnhandledTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        //final List<String> notificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
        //final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(relationShipNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(SALES_MANAGER_NOTIFICATION_LIST, salesManagerNotificationList);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 73.72));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map = (Map<String, CreditTenantScoreResult>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(73.72, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.ADDRESS_CHECK_RESULT));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void salesManagerRejectsTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        final List<String> notificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
        final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(notificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.rejected(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_REJECTED.class.getSimpleName());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verifyNoInteractions(creditScoreServiceMockBean);
        verifyNoInteractions(fraudPreventionServiceMockBean);
        verifyNoInteractions(addressCheckServiceMockBean);
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void notSufficientCreditScoreTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        final List<String> notificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
        final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(notificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 64.63));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(),
                instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map =
                (Map<String, CreditTenantScoreResult>) report
                        .state()
                        .controlObject()
                        .get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(64.63, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(),
                instanceOf(
                        CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_FROM_SENIOR_MANAGER.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.ADDRESS_CHECK_RESULT));
        verify(notifierService, times(1)).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        creditSMFacade.acceptableScore(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.accepted(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_ACCEPTED.class.getSimpleName());
        verify(notifierService, times(1)).notify(eq(List.of(customer.getEmail())), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void creditScoreLowRejectTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        final List<String> notificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
        payload.put(RELATIONSHIP_MANAGER_NOTIFICATION_LIST, notificationList);

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(notificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");
        payload.put(SALES_MANAGER_NOTIFICATION_LIST, salesManagerNotificationList);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 28.91));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map = (Map<String, CreditTenantScoreResult>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(28.91, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_REJECTED.class.getSimpleName());
        assertEquals(true, creditSmEs.get().getAddressCheckResult());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());
        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void addressCheckResultReceviedFirstTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        final List<String> notificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
        final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(notificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_ADRRESCHECK_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.ADDRESS_CHECK_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 73.72));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_ADDRESSCHECK_RESULT_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map = (Map<String, CreditTenantScoreResult>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(73.72, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.accepted(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_ACCEPTED.class.getSimpleName());
        assertEquals(true, creditSmEs.get().getAddressCheckResult());
        verify(notifierService, times(1)).notify(eq(List.of(customer.getEmail())), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());
        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    @Test
    @SneakyThrows
    public void customerRelationshipAdapterTest() {
        final String creditUuid = UUID.randomUUID().toString();
        final Customer customer =
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
        final List<Customer> creditTenants = new ArrayList<>();
        creditTenants.add(customer);
        Map<String, Object> payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.submit(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Future<Response> futureCreditSMState = creditSMFacade.currentState(payload);
        ReportResponse report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL.class));
        assertEquals(((List<Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0), customer);
        verify(notifierService, times(1)).notify(eq(relationShipNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);

        creditSMFacade.relationshipManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED.class));
        verify(notifierService, times(1)).notify(eq(salesManagerNotificationList), anyString());

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.salesManagerApproved(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_INITIAL_CSC.class));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer.getPersonalId(),
                new CreditTenantScoreResult(customer.getPersonalId(), 73.72));
        payload.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);
        payload.put(SOURCE_SLAVE_SM_TAG, CUSTOMER_SCORE_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_RECEIVED.class));
        Map<String, CreditTenantScoreResult> map = (Map<String, CreditTenantScoreResult>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS);
        assertEquals(73.72, map.get(customer.getPersonalId()).getCreditScore());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.FRAUD_PREVENTION_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, FRAUD_PREVENTION_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        //Customer Relationship Adapter
        final Customer updatedCustomer = customer.toBuilder().firstName("UpdatedJohn").lastName("UpdatedDoe").build();
        final Map<String, Object> customerRelationShipAdapterPayload = new HashMap<>();
        customerRelationShipAdapterPayload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        customerRelationShipAdapterPayload.put(PayloadVariableConstants.CUSTOMER, updatedCustomer);

        creditSMFacade.customerUpdated(customerRelationShipAdapterPayload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_SALES_MANAGER_APPROVED_$_CREDITSCORE_FRAUDPREVENTION_RESULT_RECEIVED.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.FRAUD_PREVENTION_RESULT));
        List<Customer> checkCreditTenants = (List<Customer>) report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS);
        assertEquals("UpdatedJohn", checkCreditTenants.get(0).getFirstName());
        assertEquals("UpdatedDoe", checkCreditTenants.get(0).getLastName());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        payload.put(PayloadVariableConstants.ADDRESS_CHECK_RESULT, true);
        payload.put(SOURCE_SLAVE_SM_TAG, ADDRESS_CHECK_SM);
        creditSMFacade.resultReceived(payload);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        futureCreditSMState = creditSMFacade.currentState(payload);

        report =
                (ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL.class));
        assertEquals(true, report.state().controlObject().get(PayloadVariableConstants.ADDRESS_CHECK_RESULT));
        verify(notifierService, times(1)).notify(eq(creditAnalystNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = preparePayload(creditUuid, creditTenants);
        creditSMFacade.accepted(payload);

        Thread.sleep(15000L);

        Optional<CreditSmEs> creditSmEs = creditSMRepository.findById(creditUuid);

        assertNotNull(creditSmEs);
        assertEquals(creditSmEs.get().getState(), CREDIT_ACCEPTED.class.getSimpleName());
        assertEquals(true, creditSmEs.get().getFraudPreventionResult());
        verify(notifierService, times(1)).notify(eq(List.of(customer.getEmail())), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        verify(creditScoreServiceMockBean).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());
        Thread.sleep(WAIT_TIME_ELASTIC);
    }

    private Map<String, Object> preparePayload(
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