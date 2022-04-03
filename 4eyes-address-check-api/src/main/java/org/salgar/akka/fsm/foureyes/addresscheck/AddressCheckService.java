package org.salgar.akka.fsm.foureyes.addresscheck;

public interface AddressCheckService {
    void addressExist(String street, String houseNo, String city, String country);
}