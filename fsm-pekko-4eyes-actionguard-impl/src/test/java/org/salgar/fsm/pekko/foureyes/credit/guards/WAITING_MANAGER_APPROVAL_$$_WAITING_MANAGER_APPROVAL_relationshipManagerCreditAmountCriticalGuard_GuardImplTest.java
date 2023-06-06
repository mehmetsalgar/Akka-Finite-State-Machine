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
import org.salgar.pekko.fsm.foureyes.notifier.NotifierService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_AMOUNT;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.CREDIT_APPLICATION;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED;
import static org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants.RELATIONSHIP_MANAGERS;

@ExtendWith(MockitoExtension.class)
public class WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImplTest {
    List<String> relationShipNotificationList = Arrays.asList("relationshipmanager1@example.com", "relationshipmanager2@example.com");

    @Mock
    private ActorContext<CreditSM.CreditSMEvent> actorContext;

    @Mock
    private Logger log;

    @Test
    public void twoManagerApprovalTest() {
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
          new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, relationShipNotificationList);
        controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void oneManagerApprovalTest() {
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
                new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, relationShipNotificationList);
        //controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertTrue(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void threeToOneManagerApprovalTest() {
        List<String> myRelationShipNotificationList = new ArrayList<>(relationShipNotificationList);
        myRelationShipNotificationList.add("relationshipmanager3@example.com");
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
                new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, myRelationShipNotificationList);
        controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 1);

        when(actorContext.log()).thenReturn(log);

        assertTrue(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void threeToTwoManagerApprovalTest() {
        List<String> myRelationShipNotificationList = new ArrayList<>(relationShipNotificationList);
        myRelationShipNotificationList.add("relationshipmanager3@example.com");
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
                new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, myRelationShipNotificationList);
        controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 2);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void onlyOneManagerApprovalTest() {
        List<String> myRelationShipNotificationList = new ArrayList<>();
        myRelationShipNotificationList.add("relationshipmanager1@example.com");
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
                new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, myRelationShipNotificationList);
        controlObject.put(NUMBER_OF_RELATIONSHIP_MANAGERS_APPROVED, 2);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }

    @Test
    public void belowAmountLevelApprovalTest() {
        NotifierService notifierService = new NotifierService() {
            @Override
            public void notify(List<String> notificationTargets, String message) {

            }

            @Override
            public List<String> calculateRecipientList(String targetGroup) {
                return null;
            }
        };
        WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl guard =
                new WAITING_MANAGER_APPROVAL_$$_WAITING_MANAGER_APPROVAL_relationshipManagerCreditAmountCriticalGuard_GuardImpl();

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
        controlObject.put(RELATIONSHIP_MANAGERS, relationShipNotificationList);

        when(actorContext.log()).thenReturn(log);

        assertFalse(guard.evaluate(actorContext, controlObject, payload));
    }
}