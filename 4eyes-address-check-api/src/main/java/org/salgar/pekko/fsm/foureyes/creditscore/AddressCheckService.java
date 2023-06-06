package org.salgar.pekko.fsm.foureyes.creditscore;

public interface AddressCheckService {
    void addressExist(String street, String houseNo, String city, String country);
}