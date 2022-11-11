package ua.pz33.clients;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.utils.logs.LogMediator;

import java.util.Comparator;
import java.util.List;

public class Client {
    private Integer id;
    private Integer countOfTickets;
    private ClientStatus status;

    public Client(int id, int countOfTickets, ClientStatus status){
        this.id = id;
        this.countOfTickets = countOfTickets;
        this.status = status;
    }

    public void buyTickets(){
        var message = String.format("Client %d bought %d tickets", id, countOfTickets);
        LogMediator.getInstance().logMessage(message);
    }

    public void chooseCashRegister(List<CashRegister> cashRegisters){
        //check if all cash registers have same number of people in queue
        var sortedCashRegisters = cashRegisters.stream().filter(c -> c.isOpen()).sorted(countInQueueComparator).toList();

        var firstCashRegister =sortedCashRegisters.get(0);
        var count = firstCashRegister.getClientsQueue().size();
        var allHaveSameCount = sortedCashRegisters.stream().allMatch(c -> c.getClientsQueue().size() == count);

        if(allHaveSameCount){
            //todo choose the closest
            //cashRegister.addToQueue(this);
        }
        firstCashRegister.tryAddToQueue(this);
    }

    public static Comparator<CashRegister> countInQueueComparator = (c1, c2) -> (int) (c1.getClientsQueue().size() - c2.getClientsQueue().size());

    public Integer getId() {
        return id;
    }

    public Integer getCountOfTickets() {
        return countOfTickets;
    }

    public ClientStatus getStatus() {
        return status;
    }
}

