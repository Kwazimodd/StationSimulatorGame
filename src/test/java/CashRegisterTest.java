import org.junit.jupiter.api.Assertions;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;

class CashRegisterTest {

    @org.junit.jupiter.api.Test
    void clientsQueueSortId() {
        CashRegister cashRegister = new CashRegister();
        cashRegister.tryAddToQueue(new Client(1, 3000, ClientStatus.REGULAR));
        cashRegister.tryAddToQueue(new Client(4, 3000, ClientStatus.REGULAR));
        cashRegister.tryAddToQueue(new Client(2, 4000, ClientStatus.HAS_KIDS));
        cashRegister.tryAddToQueue(new Client(3, 4000, ClientStatus.HAS_KIDS));
        cashRegister.tryAddToQueue(new Client(0, 2000, ClientStatus.INVALID));

        var client = cashRegister.getClientsQueue().poll();
        Assertions.assertTrue(client.getStatus() == ClientStatus.INVALID);

        client = cashRegister.getClientsQueue().poll();
        Assertions.assertTrue(client.getId() == 2);

        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getStatus() == ClientStatus.HAS_KIDS);
        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getId() == 1);
        Assertions.assertTrue(cashRegister.getClientsQueue().poll().getStatus() == ClientStatus.REGULAR);
    }
}