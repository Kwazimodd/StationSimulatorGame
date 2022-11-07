package ua.pz33.cashregisters;

import org.junit.jupiter.api.Assertions;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {

    @org.junit.jupiter.api.Test
    void getClientsQueue() {
        CashRegister cashRegister = new CashRegister();
        cashRegister.addToQueue(new Client(1, 3000, ClientStatus.REGULAR));
        cashRegister.addToQueue(new Client(2, 4000, ClientStatus.HAS_KIDS));
        cashRegister.addToQueue(new Client(0, 2000, ClientStatus.INVALID));

        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getStatus() == ClientStatus.INVALID);
        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getStatus() == ClientStatus.HAS_KIDS);
        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getStatus() == ClientStatus.REGULAR);
    }
}