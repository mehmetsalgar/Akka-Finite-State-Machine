package org.salgar.fsm.pekko.foureyes.credit.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salgar.fsm.pekko.foureyes.credit.CreditSM;
import org.salgar.fsm.pekko.foureyes.credit.model.Address;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditApplication;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenants;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_ANALYSTS;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_APPLICATION;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_CREDIT_ANALYST_APPROVED;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.SALES_MANAGERS;

@ExtendWith(MockitoExtension.class)
public class WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImplTest {
    final List<String> creditAnalystNotificationList = List.of("creditanalyst@example.com");

    @Mock
    private ActorContext<CreditSM.CreditSMEvent> actorContext;

    @Mock
    private Logger log;

    @Test
    public void twoManagerApprovalTest() {
        List<String> myCreditAnalystNotification = new ArrayList<>(creditAnalystNotificationList);
        myCreditAnalystNotification.add("creditanalyst2@example.com");
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, myCreditAnalystNotification);
        controlObject.put(NUMBER_OF_CREDIT_ANALYST_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void oneManagerApprovalTest() {
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, creditAnalystNotificationList);
        //controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void twoManagerOneApprovedTest() {
        List<String> myCreditAnalystNotification = new ArrayList<>(creditAnalystNotificationList);
        myCreditAnalystNotification.add("creditanalyst2@example.com");
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, myCreditAnalystNotification);
        //controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertTrue(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void threeToOneManagerApprovalTest() {
        List<String> myCreditAnalystNotificationList = new ArrayList<>(creditAnalystNotificationList);
        myCreditAnalystNotificationList.add("creditanalyst2@example.com");
        myCreditAnalystNotificationList.add("creditanalyst3@example.com");
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, myCreditAnalystNotificationList);
        controlObject.put(NUMBER_OF_CREDIT_ANALYST_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertTrue(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void threeToTwoManagerApprovalTest() {
        List<String> myCreditAnalystNotificationList = new ArrayList<>(creditAnalystNotificationList);
        myCreditAnalystNotificationList.add("creditanaylst2@example.com");
        myCreditAnalystNotificationList.add("creditanalyst3@example.com");
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, myCreditAnalystNotificationList);
        controlObject.put(NUMBER_OF_CREDIT_ANALYST_APPROVED, 2);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void onlyOneManagerApprovalTest() {
        List<String> mySalesNotificationList = new ArrayList<>();
        mySalesNotificationList.add("creditanalyst11@example.com");
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                20000000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 20000000.0);
        controlObject.put(CREDIT_ANALYSTS, mySalesNotificationList);
        controlObject.put(NUMBER_OF_CREDIT_ANALYST_APPROVED, 2);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void belowAmountLevelApprovalTest() {
        WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl guard =
                new WAITING_ANAYLIST_APPROVAL_$$_WAITING_ANAYLIST_APPROVAL_creditAnalystCreditAmountCritical_GuardImpl();

        Map<String, Object> controlObject = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

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
        CreditApplication creditApplication = new CreditApplication(
                100000.0,
                new CreditTenants(creditTenants)
        );
        controlObject.put(CreditUseCaseKeyStrategy.CREDIT_UUID, UUID.randomUUID().toString());
        controlObject.put(CREDIT_APPLICATION, creditApplication);
        controlObject.put(CREDIT_AMOUNT, 100000.0);
        controlObject.put(SALES_MANAGERS, creditAnalystNotificationList);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }
}