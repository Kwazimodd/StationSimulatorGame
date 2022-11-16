package ua.pz33;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.sprites.Entrance;
import ua.pz33.sprites.Exit;

import java.util.*;

public class Station {
    private static Station instance;

    public static Station getInstance() {
        if (instance == null) {
            instance = new Station();
        }

        return instance;
    }
    public void moveQueue(PriorityQueue<Client> clientsQueue){
        // Find backup cash register with minimum queue size.
        Optional<CashRegister> bestBackupCashRegister = backupCashRegisters.stream().min(
                Comparator.comparingInt(cashRegister -> cashRegister.getClientsQueue().size()));

        if (bestBackupCashRegister.isEmpty()){
            clientsQueue.forEach(client -> client.chooseCashRegister(cashRegisters));
        }else{
            bestBackupCashRegister.get().open();
            clientsQueue.forEach(bestBackupCashRegister.get()::tryAddToQueue);
        }

        clientsQueue.clear();
    }

    public void addEntrance(Entrance entrance){
        entrances.add(entrance);
    }

    public void addExit(Exit newExit){
        exit = newExit;
    }

    public void addCashRegister(CashRegister cashRegister){
        cashRegisters.add(cashRegister);
    }

    public void addBackupCashRegister(CashRegister cashRegister){
        cashRegister.makeBackup();
        backupCashRegisters.add(cashRegister);
    }

    private List<CashRegister> cashRegisters = new ArrayList<>();
    private List<CashRegister> backupCashRegisters = new ArrayList<>();
    private List<Entrance> entrances = new ArrayList<>();
    private Exit exit;
}
