package ua.pz33;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.sprites.Entrance;
import ua.pz33.sprites.Exit;
import ua.pz33.utils.clock.GameClock;

import java.util.*;

public class Station {
    private List<CashRegister> cashRegisters = new ArrayList<>();
    private List<CashRegisterSprite> cashRegisterSprites = new ArrayList<>();
    private List<CashRegister> backupCashRegisters = new ArrayList<>();
    private List<CashRegisterSprite> backupCashRegisterSprites = new ArrayList<>();
    private List<Entrance> entrances = new ArrayList<>();
    private Exit exit;
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
            clientsQueue.forEach(client -> client.tryChooseCashRegister(cashRegisters));
        }else{
            bestBackupCashRegister.get().open();
            clientsQueue.forEach(bestBackupCashRegister.get()::tryAddToQueue);
        }

        clientsQueue.clear();
    }

    public void addEntrance(Entrance entrance){
        entrances.add(entrance);
    }
    public List<Entrance> getEntrances(){
        return entrances;
    }

    public void addExit(Exit newExit){
        exit = newExit;
    }
    public Exit getExit(){
        return exit;
    }

    public void addCashRegister(int x, int y){
        CashRegister cashRegister = new CashRegister();
        GameClock.getInstance().addObserver(cashRegister);
        cashRegisters.add(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegister200X200.png");
        cashRegisterSprite.setX(x);
        cashRegisterSprite.setY(y);
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.add(cashRegisterSprite);
    }

    public void addBackupCashRegister(int x, int y){
        CashRegister cashRegister = new CashRegister();
        cashRegister.makeBackup();
        GameClock.getInstance().addObserver(cashRegister);
        backupCashRegisters.add(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegisterReserved200X200.png");
        cashRegisterSprite.setX(x);
        cashRegisterSprite.setY(y);
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.add(cashRegisterSprite);
    }

    public void addCashRegister(CashRegister cashRegister){
        cashRegisters.add(cashRegister);
    }

    public void addBackupCashRegister(CashRegister cashRegister){
        cashRegister.makeBackup();
        backupCashRegisters.add(cashRegister);
    }

    public List<CashRegister> getCashRegisters(){
        return cashRegisters;
    }

    public List<CashRegisterSprite> getCashRegisterSprites(){
        return cashRegisterSprites;
    }

    public List<CashRegister> getBackupCashRegisters(){
        return backupCashRegisters;
    }
    public CashRegister getCashRegister(int id){
        return backupCashRegisters.stream().filter(c -> c.getId() == id).findFirst().get();
    }
    public CashRegisterSprite getCashRegisterSprite(int id){
        return cashRegisterSprites.stream().filter(c -> c.getId() == id).findFirst().get();
    }

    public CashRegisterSprite getBackupCashRegisterSprite(int id){
        return backupCashRegisterSprites.stream().filter(c -> c.getId() == id).findFirst().get();
    }
}
