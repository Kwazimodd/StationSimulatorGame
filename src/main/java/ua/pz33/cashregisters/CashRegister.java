package ua.pz33.cashregisters;

import ua.pz33.clients.Client;
import ua.pz33.utils.clock.GameClock;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CashRegister {
    private PriorityQueue<Client> clientsQueue = new PriorityQueue<>(statusComparator);
    private int secondsToServeClient = 10;
    private boolean isOpen = true;

    public CashRegister(){

    }

    public CashRegister(PriorityQueue<Client> oldQueue){
        clientsQueue = oldQueue;
    }

    public boolean tryAddToQueue(Client client){
        if (!isOpen){
            return false;
        }

        clientsQueue.add(client);
        return true;
    }

    public void service(){
        //// TODO: 07.11.2022 add time for client service from configuration

        GameClock.getInstance().postExecute(secondsToServeClient, () -> {
            var currentClient = clientsQueue.poll();
            currentClient.buyTickets();
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
        isOpen = true;
    }

    public void close(){
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getSecondsToServeClient() {
        return secondsToServeClient;
    }

    public void setSecondsToServeClient(int secondsToServeClient) {
        this.secondsToServeClient = secondsToServeClient;
    }
}