package ua.pz33.cashregisters;

import ua.pz33.Station;
import ua.pz33.clients.Client;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.clock.GameClock;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CashRegister implements ClockObserver {
    private static int CashRegisterId = 1;
    private PriorityQueue<Client> clientsQueue = new PriorityQueue<>(statusComparator);
    private int ticksToServeClient = 50;
    private boolean isBackup = false;
    private int id;

    private CashRegisterState currentState;

    public CashRegister(){
        currentState = CashRegisterState.Waiting;
        id = CashRegisterId++;
    }

    public CashRegister(PriorityQueue<Client> oldQueue){
        clientsQueue = oldQueue;
    }

    public boolean tryAddToQueue(Client client){
        if (currentState.equals(CashRegisterState.Closed)){
            return false;
        }

        clientsQueue.add(client);
        return true;
    }

    public void service(){
        // todo add time for client service from configuration
        if(currentState.equals(CashRegisterState.Servicing))
            return;

        currentState = CashRegisterState.Servicing;
        GameClock.getInstance().postExecute(ticksToServeClient, () -> {
            var currentClient = clientsQueue.poll();
            currentClient.buyTickets();
            currentState = CashRegisterState.Waiting;

            if (isBackup && clientsQueue.isEmpty()){
                close();
            }
        });

    }

    public PriorityQueue<Client> getClientsQueue(){
        return clientsQueue;
    }

    public static Comparator<Client> statusComparator = (c1, c2) -> {
        var statusCompareResult = (int) (c1.getStatus().compareTo(c2.getStatus()));
        if(statusCompareResult == 0){
            return (int) (c1.getId().compareTo(c2.getId()));
        }

        return statusCompareResult;
    };

    public void open(){
        currentState = CashRegisterState.Open;
    }

    public void close(){
        currentState = CashRegisterState.Closed;
        Station.getInstance().moveQueue(clientsQueue);
    }

    public boolean isOpen() {
        return !currentState.equals(CashRegisterState.Closed);
    }

    public int getTicksToServeClient() {
        return ticksToServeClient;
    }

    public void setTicksToServeClient(int ticksToServeClient) {
        this.ticksToServeClient = ticksToServeClient;
    }

    public void makeBackup(){
        isBackup = true;
        close();
    }

    public int getId(){
        return id;
    }

    @Override
    public void onTick() {
        service();
    }
}