import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void chooseCashRegister() {
        ArrayList<CashRegister> cashRegisters = new ArrayList<CashRegister>();
        cashRegisters.add(new CashRegister());
        cashRegisters.add(new CashRegister());
        cashRegisters.add(new CashRegister());
        cashRegisters.get(1).close();

        cashRegisters.get(0).tryAddToQueue(new Client(0, 2000, ClientStatus.INVALID));
        cashRegisters.get(0).tryAddToQueue(new Client(1, 3000, ClientStatus.REGULAR));
        cashRegisters.get(0).tryAddToQueue(new Client(2, 4000, ClientStatus.HAS_KIDS));

        cashRegisters.get(1).tryAddToQueue(new Client(3, 2000, ClientStatus.INVALID));

        cashRegisters.get(2).tryAddToQueue(new Client(6, 2000, ClientStatus.INVALID));
        cashRegisters.get(2).tryAddToQueue(new Client(7, 3000, ClientStatus.REGULAR));

        Client client = new Client(9, 4000, ClientStatus.HAS_KIDS);
        client.tryChooseCashRegister(cashRegisters);

        Assertions.assertTrue(cashRegisters.get(2).getClientsQueue().contains(client));
    }
}