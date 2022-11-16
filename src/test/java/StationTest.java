import org.junit.jupiter.api.Assertions;
import ua.pz33.Station;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;

public class StationTest {
    @org.junit.jupiter.api.Test
    void moveQueueTest() {
        CashRegister cashRegister1 = new CashRegister();
        cashRegister1.tryAddToQueue(new Client(4, 3000, ClientStatus.REGULAR));
        cashRegister1.tryAddToQueue(new Client(1, 3000, ClientStatus.REGULAR));
        cashRegister1.tryAddToQueue(new Client(2, 4000, ClientStatus.HAS_KIDS));
        cashRegister1.tryAddToQueue(new Client(3, 4000, ClientStatus.HAS_KIDS));
        cashRegister1.tryAddToQueue(new Client(0, 2000, ClientStatus.INVALID));

        CashRegister cashRegister2 = new CashRegister();
        cashRegister2.tryAddToQueue(new Client(5, 3000, ClientStatus.REGULAR));
        cashRegister2.tryAddToQueue(new Client(6, 3000, ClientStatus.REGULAR));

        CashRegister backupCashRegister1 = new CashRegister();
        CashRegister backupCashRegister2 = new CashRegister();

        Station station = Station.getInstance();
        station.addCashRegister(cashRegister1);
        station.addCashRegister(cashRegister2);
        station.addBackupCashRegister(backupCashRegister1);
        station.addBackupCashRegister(backupCashRegister2);

        cashRegister2.close();
        cashRegister1.close();

        Assertions.assertEquals(0, cashRegister1.getClientsQueue().size());
        Assertions.assertEquals(2, backupCashRegister1.getClientsQueue().size());
        Assertions.assertEquals(5, backupCashRegister2.getClientsQueue().size());

        Assertions.assertEquals(0, (int) backupCashRegister2.getClientsQueue().poll().getId());
        Assertions.assertEquals(2, (int) backupCashRegister2.getClientsQueue().poll().getId());
        Assertions.assertEquals(3, (int) backupCashRegister2.getClientsQueue().poll().getId());
        Assertions.assertEquals(1, (int) backupCashRegister2.getClientsQueue().poll().getId());
        Assertions.assertEquals(4, (int) backupCashRegister2.getClientsQueue().poll().getId());
    }

}
