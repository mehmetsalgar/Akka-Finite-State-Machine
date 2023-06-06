package org.salgar.fsm.akka.foureyes.creditscore.guards;

import org.apache.pekko.actor.typed.scaladsl.ActorContext;
import org.junit.jupiter.api.Test;
import org.salgar.fsm.pekko.foureyes.credit.model.Address;
import org.salgar.fsm.pekko.foureyes.credit.model.CreditTenantScoreResult;
import org.salgar.fsm.pekko.foureyes.credit.model.Customer;
import org.salgar.fsm.pekko.foureyes.creditscore.MultiTenantCreditScoreSM;
import org.salgar.fsm.pekko.foureyes.creditscore.guards.WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl;
import org.salgar.fsm.pekko.foureyes.usecasekey.CreditUseCaseKeyStrategy;
import org.salgar.fsm.pekko.foureyes.variables.PayloadVariableConstants;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WAITING_MULTI_TENANT_RESULTS_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImplTest {
    private final WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl guard =
            new WAITING_MULTI_TENANT_RESULTS_$$_FINALSTATE_isAllCreditScoreMultitenantResponded_GuardImpl();
    @Test
    public void tenantSizeOneResultReceivedTest() {
        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<Customer> creditTenants = new ArrayList<>();
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
                        "max@muster.com");
        creditTenants.add(customer);

        Map<String, Object> controlObjects = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.PERSONAL_ID, "123456789X");
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 84.51);
        boolean result = guard.evaluate(
                actorContext,
                controlObjects,
                payload);

        assertTrue(result);
    }

    @Test
    public void tenantSizeFiveOneResultReceivedTest() {
        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<Customer> creditTenants = new ArrayList<>();
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
                        "max@muster.com");
        final Customer customer2 =
                new Customer(
                        "Max",
                        "Mustermann",
                        "T295649569",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max2@muster.com");
        final Customer customer3 =
                new Customer(
                        "Darth",
                        "Vader",
                        "F356730g435",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max3@muster.com");
        final Customer customer4 =
                new Customer(
                        "Luke",
                        "Skywalker",
                        "K659453WE456t",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max4@muster.com");
        final Customer customer5 =
                new Customer(
                        "Bo",
                        "Katan",
                        "O304875035EWK04",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max5@muster.com");
        creditTenants.add(customer1);
        creditTenants.add(customer2);
        creditTenants.add(customer3);
        creditTenants.add(customer4);
        creditTenants.add(customer5);

        Map<String, Object> controlObjects = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.PERSONAL_ID, "K659453WE456t");
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);

        boolean result = guard.evaluate(
                actorContext,
                controlObjects,
                payload);

        assertFalse(result);
    }

    @Test
    public void tenantSizeFiveThreeResultExistOneResultReceivedTest() {
        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<Customer> creditTenants = new ArrayList<>();
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
                        "max@muster.com");
        final Customer customer2 =
                new Customer(
                        "Max",
                        "Mustermann",
                        "T295649569",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max2@muster.com");
        final Customer customer3 =
                new Customer(
                        "Darth",
                        "Vader",
                        "F356730g435",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max3@muster.com");
        final Customer customer4 =
                new Customer(
                        "Luke",
                        "Skywalker",
                        "K659453WE456t",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max4@muster.com");
        final Customer customer5 =
                new Customer(
                        "Bo",
                        "Katan",
                        "O304875035EWK04",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max5@muster.com");
        creditTenants.add(customer1);
        creditTenants.add(customer2);
        creditTenants.add(customer3);
        creditTenants.add(customer4);
        creditTenants.add(customer5);

        Map<String, Object> controlObject = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer1.getPersonalId(), new CreditTenantScoreResult(customer1.getPersonalId(), 91.54));
        creditTenantScoreResultMap.put(
                customer2.getPersonalId(), new CreditTenantScoreResult(customer2.getPersonalId(), 11.69));
        creditTenantScoreResultMap.put(
                customer3.getPersonalId(), new CreditTenantScoreResult(customer3.getPersonalId(), 62.72));

        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.PERSONAL_ID, customer4.getPersonalId());
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);
        boolean result = guard.evaluate(
                actorContext,
                controlObject,
                payload);

        assertFalse(result);
    }

    @Test
    public void tenantSizeFiveFourResultExistOneResultReceivedTest() {
        final ActorContext<MultiTenantCreditScoreSM.MultiTenantCreditScoreSMEvent> actorContext =
                mock(ActorContext.class);
        final Logger log = mock(Logger.class);

        when(actorContext.log()).thenReturn(log);

        final List<Customer> creditTenants = new ArrayList<>();
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
                        "max5@muster.com");
        final Customer customer2 =
                new Customer(
                        "Max",
                        "Mustermann",
                        "T295649569",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max2@muster.com");
        final Customer customer3 =
                new Customer(
                        "Darth",
                        "Vader",
                        "F356730g435",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max3@muster.com");
        final Customer customer4 =
                new Customer(
                        "Luke",
                        "Skywalker",
                        "K659453WE456t",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max4@muster.com");
        final Customer customer5 =
                new Customer(
                        "Bo",
                        "Katan",
                        "O304875035EWK04",
                        new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        ),
                        "max5@muster.com");
        creditTenants.add(customer1);
        creditTenants.add(customer2);
        creditTenants.add(customer3);
        creditTenants.add(customer4);
        creditTenants.add(customer5);

        Map<String, Object> controlObject = prepareControlObject(
                UUID.randomUUID().toString(),
                creditTenants
        );
        Map<String, CreditTenantScoreResult> creditTenantScoreResultMap = new HashMap<>();
        creditTenantScoreResultMap.put(
                customer1.getPersonalId(), new CreditTenantScoreResult(customer1.getPersonalId(), 91.54));
        creditTenantScoreResultMap.put(
                customer2.getPersonalId(), new CreditTenantScoreResult(customer2.getPersonalId(), 11.69));
        creditTenantScoreResultMap.put(
                customer3.getPersonalId(), new CreditTenantScoreResult(customer3.getPersonalId(), 62.72));
        creditTenantScoreResultMap.put(
                customer5.getPersonalId(), new CreditTenantScoreResult(customer5.getPersonalId(), 62.72));

        controlObject.put(PayloadVariableConstants.CREDIT_SCORE_TENANT_RESULTS, creditTenantScoreResultMap);

        Map<String, Object> payload = new HashMap<>();
        payload.put(PayloadVariableConstants.PERSONAL_ID, customer4.getPersonalId());
        payload.put(PayloadVariableConstants.CREDIT_SCORE_RESULT, 66.83);
        boolean result = guard.evaluate(
                actorContext,
                controlObject,
                payload);

        assertTrue(result);
    }

    private Map<String, Object> prepareControlObject(
            String creditUuid,
            List<Customer> creditTenants) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put(CreditUseCaseKeyStrategy.CREDIT_UUID, creditUuid);
        payload.put(PayloadVariableConstants.CREDIT_TENANTS, creditTenants);

        return payload;
    }
}