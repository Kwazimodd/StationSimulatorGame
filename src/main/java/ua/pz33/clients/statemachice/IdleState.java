package ua.pz33.clients.statemachice;

import ua.pz33.StationController;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;

import java.util.Collection;
import java.util.List;

public class IdleState extends State {
    public IdleState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        Collection<CashRegister> cashRegisters = StationController.getInstance().getCashRegisters();
        if (client.tryChooseCashRegister(cashRegisters)) {
            client.changeState(new MovingState(client));
        }
    }
}
