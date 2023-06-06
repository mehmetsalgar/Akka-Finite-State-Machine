package org.salgar.fsm.pekko.foureyes.kafka;

import com.google.protobuf.Any;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salgar.fsm.pekko.converter.service.ConverterService;
import org.salgar.fsm.pekko.foureyes.TestApplication;
import org.salgar.fsm.pekko.foureyes.addresscheck.actions.config.AdressCheckSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.addresscheck.command.AdressCheckSMCommandConstants;
import org.salgar.fsm.pekko.foureyes.addresscheck.guards.config.AdressCheckSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AddressCheckResult;
import org.salgar.fsm.pekko.foureyes.addresscheck.protobuf.AdressCheckSMCommand;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.actions.config.CreditSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.command.CreditSMCommandConstants;
import org.salgar.fsm.pekko.foureyes.credit.facade.CreditSMFacade;
import org.salgar.fsm.pekko.foureyes.credit.guards.config.CreditSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.credit.kafka.config.TopicProperties;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditApplication;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditSMCommand;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditTenants;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.CreditUUID;
import org.salgar.fsm.pekko.foureyes.credit.protobuf.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.actions.config.CreditScoreSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.command.CreditScoreSMCommandConstants;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.config.CreditScoreSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreResult;
import org.salgar.fsm.pekko.foureyes.creditscore.protobuf.CreditScoreSMCommand;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.config.FraudPreventionSMActionConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.actions.guards.config.FraudPreventionSMGuardConfiguration;
import org.salgar.fsm.pekko.foureyes.fraudprevention.command.FraudPreventionSMCommandConstants;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionResult;
import org.salgar.fsm.pekko.foureyes.fraudprevention.protobuf.FraudPreventionSMCommand;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;
import org.salgar.pekko.fsm.foureyes.creditscore.AddressCheckService;
import org.salgar.pekko.fsm.foureyes.creditscore.CreditScoreService;
import org.salgar.pekko.fsm.foureyes.faudprevention.FraudPreventionService;
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.salgar.fsm.pekko.converter.Protobuf2PojoConverter.PROTOBUF_PREFIX;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.CREDIT_ANALYST_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.RELATIONSHIP_MANAGER_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.SALES_MANAGER_NOTIFICATION_LIST;
import static org.salgar.pekko.fsm.foureyes.notifier.NotificationHelper.SENIOR_SALES_MANAGER_NOTIFICATION_LIST;

@Disabled
@ActiveProfiles({"itest"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        topics = {"creditSM", "creditScoreSM", "addressCheckSM", "fraudPreventionSM", "multiTenantScreditScoreSM"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Import({
        AdressCheckSMActionConfiguration.class,
        AdressCheckSMGuardConfiguration.class,
        CreditSMActionConfiguration.class,
        CreditSMGuardConfiguration.class,
        CreditScoreSMActionConfiguration.class,
        CreditScoreSMGuardConfiguration.class,
        FraudPreventionSMActionConfiguration.class,
        FraudPreventionSMGuardConfiguration.class
})
@Slf4j
@SpringBootTest(
        classes = {TestApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaITest {
    private final static long WAIT_TIME_BETWEEN_STEPS = TimeUnit.SECONDS.toMillis(3);
    private final static long WAIT_TIME_ELASTIC = TimeUnit.SECONDS.toMillis(30);
    final List<String> relationShipNotificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");
    final List<String> salesManagerNotificationList = Arrays.asList("salesmanager1@example.com", "salesmanager2@example.com");
    final List<String> creditAnalystNotificationList = List.of("creditanalyst@example.com");
    final List<String> seniorSalesManagerNotificationList = List.of("seniorSalesManagert@example.com");
    @Autowired
    private CreditSMFacade creditSMFacade;

    @MockBean
    private CreditScoreService creditScoreServiceMockBean;

    @MockBean
    private FraudPreventionService fraudPreventionServiceMockBean;

    @MockBean
    private AddressCheckService addressCheckServiceMockBean;

    @MockBean
    private NotifierService notifierService;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private KafkaTemplate<String, CreditSMCommand> kafkaTemplateCreditSM;

    @Autowired
    private KafkaTemplate<String, CreditScoreSMCommand> kafkaTemplateCreditScoreSM;

    @Autowired
    private KafkaTemplate<String, AdressCheckSMCommand> kafkaTemplateAddressCheckSM;

    @Autowired
    private KafkaTemplate<String, FraudPreventionSMCommand> kafkaTemplateFraudPreventionSM;

    @Autowired
    private TopicProperties topicProperties;

    @BeforeEach
    public void before() {
        prepareNotificationService();
    }

    @SneakyThrows
    @Test
    public void initialTest() {
        String creditUuid = UUID.randomUUID().toString();

        kafkaTemplateCreditSM.setCloseTimeout(java.time.Duration.ofMillis(TimeUnit.MILLISECONDS.toMillis(10)));
        kafkaTemplateCreditScoreSM.setCloseTimeout(java.time.Duration.ofMillis(TimeUnit.MILLISECONDS.toMillis(10)));
        kafkaTemplateAddressCheckSM.setCloseTimeout(java.time.Duration.ofMillis(TimeUnit.MILLISECONDS.toMillis(10)));
        kafkaTemplateFraudPreventionSM.setCloseTimeout(java.time.Duration.ofMillis(TimeUnit.MILLISECONDS.toMillis(10)));

        Customer.Builder customer1Builder =
                Customer
                        .getDefaultInstance().toBuilder();
        customer1Builder
                .setFirstName("John")
                .setLastName("Doe")
                .setPersonalId("123456789X")
                .setEmail("customer1@test.info")
                .getAddressBuilder()
                .setStreet("muster strasse 1")
                .setHouseNo("11A")
                .setCity("city1")
                .setCountry("country1");

        final Customer customer1 = customer1Builder.build();

        Customer.Builder customer2Builder =
                Customer
                        .getDefaultInstance().toBuilder();
        customer2Builder
                .setFirstName("Max")
                .setLastName("Musterman")
                .setPersonalId("Z987654321")
                .setEmail("customer2@test.info")
                .getAddressBuilder()
                .setStreet("muster strasse 1")
                .setHouseNo("11A")
                .setCity("city1")
                .setCountry("country1");

        final Customer customer2 = customer2Builder.build();

        CreditTenants.Builder creditTenantsBuilder =
                CreditTenants.getDefaultInstance().toBuilder();
        creditTenantsBuilder
                .addCreditTenants(customer1)
                .addCreditTenants(customer2)
                .build();

        final CreditTenants creditTenants =
                creditTenantsBuilder.build();

        CreditApplication.Builder creditApplicationBuilder =
                CreditApplication.getDefaultInstance().toBuilder();
        creditApplicationBuilder.setCreditAmount(10000.0);
        creditApplicationBuilder.setCreditTenants(creditTenants);

        final CreditApplication creditApplication = creditApplicationBuilder.build();

        /* Mock Preparation */

        doAnswer(invocation -> {
            if(invocation.getArgument(2).equals(customer1.getPersonalId())) {
                log.info("Sending Credit Score Result customer1");

                Thread.sleep(5000L);

                CreditScoreSMCommand creditScoreSMCommand =
                        CreditScoreSMCommand
                                .newBuilder()
                                .setCommand(CreditScoreSMCommandConstants.ON_RESULTRECEIVED)
                                .setUseCaseKey(creditUuid + "_" + customer1.getPersonalId())
                                .putPayload(
                                        PayloadVariableConstants.CREDIT_SCORE_RESULT,
                                        Any.pack(CreditScoreResult.newBuilder().setCreditScore(84.21).build()))
                                .build();
                kafkaTemplateCreditScoreSM.send(
                        topicProperties.getCreditScoreSM(),
                        creditUuid + "_" + customer1.getPersonalId(),
                        creditScoreSMCommand);

            } else if(invocation.getArgument(2).equals(customer2.getPersonalId())) {
                log.info("Sending Credit Score Result customer2");

                Thread.sleep(3000L);

                CreditScoreSMCommand creditScoreSMCommand =
                        CreditScoreSMCommand
                                .newBuilder()
                                .setCommand(CreditScoreSMCommandConstants.ON_RESULTRECEIVED)
                                .setUseCaseKey(creditUuid + "_" + customer2.getPersonalId())
                                .putPayload(
                                        PayloadVariableConstants.CREDIT_SCORE_RESULT,
                                        Any.pack(CreditScoreResult.newBuilder().setCreditScore(97.45).build()))
                                .build();
                kafkaTemplateCreditScoreSM.send(
                        topicProperties.getCreditScoreSM(),
                        creditUuid + "_" + customer2.getPersonalId(),
                        creditScoreSMCommand);
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

            FraudPreventionSMCommand fraudPreventionSMCommand =
                    FraudPreventionSMCommand
                            .newBuilder()
                            .setCommand(FraudPreventionSMCommandConstants.ON_RESULT)
                            .setUseCaseKey(creditUuid)
                            .putPayload(
                                    PayloadVariableConstants.FRAUD_PREVENTION_RESULT,
                                    Any.pack(FraudPreventionResult.newBuilder().setFraudPreventionResult(true).build()))
                            .build();
            kafkaTemplateFraudPreventionSM.send(
                    topicProperties.getFraudPreventionSM(),
                    creditUuid,
                    fraudPreventionSMCommand);

            return null;
        }).when(fraudPreventionServiceMockBean).reportFraudPrevention(
                any(),
                any(),
                any());

        doAnswer(invocation -> {
            log.info("Sending Address Check Result");

            Thread.sleep(2000L);

            AdressCheckSMCommand adressCheckSMCommand = AdressCheckSMCommand
                    .newBuilder()
                    .setCommand(AdressCheckSMCommandConstants.ON_RESULT)
                    .setUseCaseKey(creditUuid)
                    .putPayload(
                            PayloadVariableConstants.ADDRESS_CHECK_RESULT,
                            Any.pack(AddressCheckResult.newBuilder().setAddressCheckResult(true).build()))
                    .build();
            kafkaTemplateAddressCheckSM.send(
                    topicProperties.getAdressCheckSM(),
                    creditUuid,
                    adressCheckSMCommand);

            return null;
        }).when(addressCheckServiceMockBean).addressExist(
                any(),
                any(),
                any(),
                any());

        CreditSMCommand creditSMSubmitCommand =
                CreditSMCommand
                        .newBuilder()
                        .setCommand(CreditSMCommandConstants.ON_SUBMIT)
                        .setUseCaseKey(creditUuid)
                        .putPayload(
                                CreditUseCaseKeyStrategy.CREDIT_UUID,
                                Any.pack(CreditUUID.newBuilder().setCreditUUID(creditUuid).build()))
                        .putPayload(
                                PayloadVariableConstants.CREDIT_APPLICATION,
                                Any.pack(creditApplication))
                        .build();

        kafkaTemplateCreditSM.send(
                topicProperties.getCreditSM(),
                creditUuid,
                creditSMSubmitCommand);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        Map<String, Object> payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);

        Future<CreditSM.Response> futureCreditSMState = creditSMFacade.currentState(payload);
        CreditSM.ReportResponse report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_APPROVAL_$_WAITING_MANAGER_APPROVAL.class));
        assertEquals(
                ((List<org.salgar.fsm.pekko.foureyes.credit.model.Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(0),
                converterService.converter(PROTOBUF_PREFIX + customer1.getDescriptorForType().getFullName()).convert(customer1));
        assertEquals(
                ((List<org.salgar.fsm.pekko.foureyes.credit.model.Customer>)report.state().controlObject().get(PayloadVariableConstants.CREDIT_TENANTS)).get(1),
                converterService.converter(PROTOBUF_PREFIX + customer1.getDescriptorForType().getFullName()).convert(customer2));
        verify(notifierService).notify(eq(relationShipNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        CreditSMCommand creditSMRelationshipManagerApprovedCommand =
                CreditSMCommand
                        .newBuilder()
                        .setCommand(CreditSMCommandConstants.ON_RELATIONSHIPMANAGERAPPROVED)
                        .setUseCaseKey(creditUuid)
                        .putPayload(
                                CreditUseCaseKeyStrategy.CREDIT_UUID,
                                Any.pack(CreditUUID.newBuilder().setCreditUUID(creditUuid).build()))
                        .putPayload(
                                PayloadVariableConstants.CREDIT_TENANTS,
                                Any.pack(creditTenants))
                        .build();

        kafkaTemplateCreditSM.send(
                topicProperties.getCreditSM(),
                creditUuid,
                creditSMRelationshipManagerApprovedCommand);

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);

        futureCreditSMState = creditSMFacade.currentState(payload);
        report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CreditSM.CREDIT_APPLICATION_SUBMITTED_$_RELATIONSHIP_MANAGER_APPROVED_$_WAITING_MANAGER_APPROVAL.class));
        verify(notifierService).notify(eq(salesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_BETWEEN_STEPS);

        CreditSMCommand creditSMSalesipManagerApprovedCommand =
                CreditSMCommand
                        .newBuilder()
                        .setCommand(CreditSMCommandConstants.ON_SALESMANAGERAPPROVED)
                        .setUseCaseKey(creditUuid)
                        .putPayload(
                                CreditUseCaseKeyStrategy.CREDIT_UUID,
                                Any.pack(CreditUUID.newBuilder().setCreditUUID(creditUuid).build()))
                        .putPayload(
                                PayloadVariableConstants.CREDIT_TENANTS,
                                Any.pack(creditTenants))
                        .build();

        kafkaTemplateCreditSM.send(
                topicProperties.getCreditSM(),
                creditUuid,
                creditSMSalesipManagerApprovedCommand);

        Thread.sleep(TimeUnit.SECONDS.toMillis(10));

        payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);

        futureCreditSMState = creditSMFacade.currentState(payload);
        report =
                (CreditSM.ReportResponse) Await.result(futureCreditSMState, Duration.create(20, TimeUnit.SECONDS));

        assertNotNull(report);
        assertThat(report.state(), instanceOf(CreditSM.CREDIT_APPLICATION_SUBMITTED_$_WAITING_CREDIT_ANALYST_APPROVAL_$_WAITING_ANAYLIST_APPROVAL.class));
        verify(notifierService).notify(eq(creditAnalystNotificationList), anyString());

        verify(creditScoreServiceMockBean, times(2)).calculateCreditScore(anyString(), anyString(), anyString());
        verify(fraudPreventionServiceMockBean).reportFraudPrevention(anyString(), anyString(), anyString());
        verify(addressCheckServiceMockBean).addressExist(anyString(), anyString(), anyString(), anyString());
        verify(notifierService, never()).notify(eq(seniorSalesManagerNotificationList), anyString());

        Thread.sleep(WAIT_TIME_ELASTIC);
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