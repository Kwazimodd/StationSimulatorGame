package ua.pz33.clients.statemachice;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;

import java.util.List;

public class IdleState extends State {
    public IdleState(Client client) {
        super(client);
    }

    @Override
    public void perform() {
        //todo get list of cashregisters
//        List<CashRegister> cashRegisters = ClientController.getInstance().getCashRegisters();
//        client.chooseCashRegister(cashRegisters);

        client.changeState(new MovingState(client));
    }
}
