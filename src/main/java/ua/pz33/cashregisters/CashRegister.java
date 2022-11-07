package ua.pz33.cashregisters;

import ua.pz33.clients.Client;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CashRegister {
    private PriorityQueue<Client> clientsQueue = new PriorityQueue<>(statusComparator);
    private boolean isOpen = true;

    public void addToQueue(Client client){
        clientsQueue.add(client);
    }

    public void service(){

    }

    public PriorityQueue<Client> getClientsQueue(){
        return clientsQueue;
    }

    public static Comparator<Client> statusComparator = (c1, c2) -> (int) (c1.getStatus().compareTo(c2.getStatus()));

    public void open(){
        isOpen = true;
    }

    public void close(){
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }
}