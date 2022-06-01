package org.salgar.fsm.akka.foureyes.creditscore.guards;

import org.salgar.fsm.akka.foureyes.credit.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class TestBase {
    protected List<CustomerV2> prepareCustomers() {
        final List<CustomerV2> creditTenants = new ArrayList<>();
        final CustomerV2 customer1 = prepareCustomerV2(
                "123456789X",
                "John",
                "Doe",
                "max@muster.com");


        final CustomerV2 customer2 = prepareCustomerV2(
                "T295649569",
                "Max",
                "Mustermann",
                "max2@muster.com");

        final CustomerV2 customer3 = prepareCustomerV2(
                "F356730g435",
                "Darth",
                "Vader",
                "max3@muster.com");

        final CustomerV2 customer4 = prepareCustomerV2(
                "K659453WE456t",
                "Luke",
                "Skywalker",
                "max4@muster.com");

        final CustomerV2 customer5 = prepareCustomerV2(
                "O304875035EWK04",
                "Bo",
                "Katan",
                "max5@muster.com");
        creditTenants.add(customer1);
        creditTenants.add(customer2);
        creditTenants.add(customer3);
        creditTenants.add(customer4);
        creditTenants.add(customer5);

        return creditTenants;
    }
    protected CustomerV2 prepareCustomerV2(String customerId, String firstName, String lastName, String email) {
        IdentificationInformation identificationInformation =
                new IdentificationInformation(
                        customerId,
                        "PASS"
                );
        IncomeProof incomeProof =
                new IncomeProof(
                        UUID.randomUUID().toString(),
                        "ABC",
                        "99999.99"
                );
        FixExpanse expanseRent =
                new FixExpanse(
                        UUID.randomUUID().toString(),
                        "1500",
                        "Rent"
                );
        FixExpanse expanseCarCredit =
                new FixExpanse(
                        UUID.randomUUID().toString(),
                        "600",
                        "Credit"
                );


        return new CustomerV2(
                customerId,
                firstName,
                lastName,
                email,
                List.of(new Address(
                        "muster strasse 1",
                        "11A",
                        "city1",
                        "country1"
                )),
                List.of(identificationInformation),
                List.of(incomeProof),
                Arrays.asList(expanseRent, expanseCarCredit)
        );
    }

    private CustomerV2 prepareCustomer() {
        IdentificationInformation identificationInformation =
                new IdentificationInformation(
                        UUID.randomUUID().toString(),
                        "PASS"
                );
        IncomeProof incomeProof =
                new IncomeProof(
                        UUID.randomUUID().toString(),
                        "ABC",
                        "99999.99"
                );
        FixExpanse expanseRent =
                new FixExpanse(
                        UUID.randomUUID().toString(),
                        "1500",
                        "Rent"
                );
        FixExpanse expanseCarCredit =
                new FixExpanse(
                        UUID.randomUUID().toString(),
                        "600",
                        "Credit"
                );

        final CustomerV2 customer =
                new CustomerV2(
                        "John",
                        "Doe",
                        "123456789X",
                        "customer1@test.info",
                        List.of(new Address(
                                "muster strasse 1",
                                "11A",
                                "city1",
                                "country1"
                        )),
                        List.of(identificationInformation),
                        List.of(incomeProof),
                        Arrays.asList(expanseRent, expanseCarCredit));

        return customer;
    }
}
