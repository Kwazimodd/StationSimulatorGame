package ua.pz33;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.sprites.ClientSprite;
import ua.pz33.sprites.Entrance;
import ua.pz33.sprites.Exit;
import ua.pz33.utils.clock.GameClock;

import java.awt.*;
import java.util.*;
import java.util.List;

public class StationController {
    private final Map<Integer, CashRegister> cashRegisters = new HashMap<>();
    private final Map<Integer, CashRegisterSprite> cashRegisterSprites = new HashMap<>();
    private final Map<Integer, CashRegister> backupCashRegisters = new HashMap<>();
    private final Map<Integer, CashRegisterSprite> backupCashRegisterSprites = new HashMap<>();
    private final List<Entrance> entrances = new ArrayList<>();
    private final Map<Integer, Client> clients = new HashMap<>();
    private final Map<Integer, ClientSprite> clientSprites = new HashMap<>();
    private Exit exit;
    private static StationController instance;

    public static StationController getInstance() {
        if (instance == null) {
            instance = new StationController();
        }

        return instance;
    }

    public void moveQueue(PriorityQueue<Client> clientsQueue) {
        // Find backup cash register with minimum queue size.
        Optional<CashRegister> bestBackupCashRegister = backupCashRegisters.values().stream().min(
                Comparator.comparingInt(cashRegister -> cashRegister.getClientsQueue().size()));

        if (bestBackupCashRegister.isEmpty()) {
            clientsQueue.forEach(client -> client.tryChooseCashRegister(cashRegisters.values()));
        } else {
            bestBackupCashRegister.get().open();
            clientsQueue.forEach(bestBackupCashRegister.get()::tryAddToQueue);
        }

        clientsQueue.clear();
    }

    public void addClient(Client client, Entrance entrance) {
        GameClock.getInstance().addObserver(client);
        clients.put(client.getId(), client);

        ClientSprite clientSprite = new ClientSprite(client);
        clientSprite.setBounds(new Rectangle(entrance.getX(), entrance.getY(), 80, 80));

        SpriteRegistry.getInstance().registerSprite(clientSprite);
        clientSprites.put(clientSprite.getId(), clientSprite);
    }

    public Collection<Client> getClients() {
        return clients.values();
    }

    public Collection<ClientSprite> getClientSprites() {
        return clientSprites.values();
    }

    public Optional<ClientSprite> getClientSprite(int id) {
        return clientSprites.values().stream().filter(clientSprite -> clientSprite.getId() == id).findFirst();
    }

    public void addEntrance(Entrance entrance) {
        SpriteRegistry.getInstance().registerSprite(entrance);
        entrances.add(entrance);
    }

    public List<Entrance> getEntrances() {
        return entrances;
    }

    public void addExit(Exit newExit) {
        SpriteRegistry.getInstance().registerSprite(newExit);
        exit = newExit;
    }

    public Exit getExit() {
        return exit;
    }

    public void addCashRegister(int x, int y) {
        CashRegister cashRegister = new CashRegister();
        cashRegisters.put(cashRegister.getId(), cashRegister);
        GameClock.getInstance().addObserver(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegister200X200.png");
        cashRegisterSprite.setBounds(new Rectangle(x, y, 100, 100));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.put(cashRegisterSprite.getId(), cashRegisterSprite);
    }

    public void addBackupCashRegister(int x, int y) {
        CashRegister cashRegister = new CashRegister();
        cashRegister.makeBackup();
        backupCashRegisters.put(cashRegister.getId(), cashRegister);
        GameClock.getInstance().addObserver(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegisterReserved200X200.png");
        cashRegisterSprite.setBounds(new Rectangle(x, y, 100, 100));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        backupCashRegisterSprites.put(cashRegister.getId(), cashRegisterSprite);
    }

    public Collection<CashRegister> getCashRegisters() {
        return cashRegisters.values();
    }

    public Collection<CashRegisterSprite> getCashRegisterSprites() {
        return cashRegisterSprites.values();
    }

    public Collection<CashRegister> getBackupCashRegisters() {
        return backupCashRegisters.values();
    }

    public CashRegister getCashRegister(int id) {
        return cashRegisters.get(id);
    }

    public CashRegisterSprite getCashRegisterSprite(int id) {
        return cashRegisterSprites.get(id);
    }

    public CashRegisterSprite getBackupCashRegisterSprite(int id) {
        return backupCashRegisterSprites.get(id);
    }
}
