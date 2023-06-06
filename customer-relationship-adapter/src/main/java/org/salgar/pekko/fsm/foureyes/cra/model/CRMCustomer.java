package org.salgar.pekko.fsm.foureyes.cra.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CRMCustomer {
    private final String firstName;
    private final String name;
}