package org.salgar.fsm.akka.foureyes.addresscheck.actions;

import akka.actor.typed.scaladsl.ActorContext;
import lombok.RequiredArgsConstructor;
import org.salgar.akka.fsm.foureyes.addresscheck.AddressCheckService;
import org.salgar.fsm.akka.foureyes.addresscheck.AdressCheckSM;
import org.salgar.fsm.akka.foureyes.credit.model.Customer;
import org.salgar.fsm.akka.foureyes.variables.PayloadVariableConstants;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class INITIAL_$$_WAITING_RESULT_initial_ActionImpl
    extends INITIAL_$$_WAITING_RESULT_initial_Action {
    private final AddressCheckService addressCheckService;

    @Override
    protected Map<String, Object> processCustomAction(ActorContext<AdressCheckSM.AdressCheckSMEvent> actorContext,
                                                        Map<String, Object> controlObject,
                                                        Map<String, Object> payload) {
        if(payload != null) {
            List<Customer> creditTenants = (List<Customer>) payload.get(PayloadVariableConstants.CREDIT_TENANTS);

            //We should actually do Multi Tenant Fraud Prevention, but living that as exercise
            Customer customer = creditTenants.get(0);
            addressCheckService.addressExist(
                    customer.getAddress().getStreet(),
                    customer.getAddress().getHouseNo(),
                    customer.getAddress().getCity(),
                    customer.getAddress().getCountry());
        }

        return payload;
    }
}
