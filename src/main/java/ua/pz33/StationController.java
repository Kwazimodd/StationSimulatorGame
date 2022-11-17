package ua.pz33;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.clients.ClientStatus;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.IntAnimation;
import ua.pz33.rendering.animation.PositionAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.sprites.ClientSprite;
import ua.pz33.sprites.Entrance;
import ua.pz33.sprites.Exit;
import ua.pz33.utils.clock.GameClock;

import java.awt.*;
import java.util.*;
import java.util.List;

public class StationController {
    private static final int SPRITE_SIZE = 50;
    private static final int SPRITE_SPACING_X = -15;
    private static final int SPRITE_SPACING_Y = 0;
    private static final int CENTER_X = 300;
    private static final int CENTER_Y = 300;

    private final AnimationController animController;

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

    private StationController() {
        animController = AnimationController.getInstance();
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

    public void addClient(Client client, Point entrance) {
        GameClock.getInstance().addObserver(client);
        clients.put(client.getId(), client);

        ClientSprite clientSprite = new ClientSprite(client);
        clientSprite.setBounds(new Rectangle((int) entrance.getX(), (int) entrance.getY(), 50, 50));

        SpriteRegistry.getInstance().registerSprite(clientSprite);
        clientSprites.put(clientSprite.getId(), clientSprite);
    }

    public Collection<Client> getClients() {
        return clients.values();
    }

    public Collection<ClientSprite> getClientSprites() {
        return clientSprites.values();
    }

    public ClientSprite getClientSprite(int id) {
        return clientSprites.get(id);
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
        cashRegisterSprite.setBounds(new Rectangle(x, y, 50, 50));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.put(cashRegisterSprite.getId(), cashRegisterSprite);

        var client4 = new Client(4, 3, ClientStatus.INVALID);
        var client6 = new Client(6, 3, ClientStatus.REGULAR);
        var client2 = new Client(2, 3, ClientStatus.REGULAR);
        var client5 = new Client(5, 3, ClientStatus.HAS_KIDS);

        addClient(client4, new Point(20, 20));
        addClient(client6, new Point(20, 20));
        addClient(client2, new Point(20, 20));
        addClient(client5, new Point(20, 20));

        cashRegister.tryAddToQueue(client4);
        cashRegister.tryAddToQueue(client6);
        cashRegister.tryAddToQueue(client2);
        cashRegister.tryAddToQueue(client5);
    }

    public void addBackupCashRegister(int x, int y) {
        CashRegister cashRegister = new CashRegister();
        cashRegister.makeBackup();
        backupCashRegisters.put(cashRegister.getId(), cashRegister);
        GameClock.getInstance().addObserver(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegisterReserved200X200.png");
        cashRegisterSprite.setBounds(new Rectangle(x, y, 100, 100));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.put(cashRegister.getId(), cashRegisterSprite);
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

    public void onQueueUpdated(CashRegister register, Collection<Client> clientsQueue) {
        var clients = new ArrayList<>(clientsQueue).stream()
                .map(this::getClientSprite)
                .toList();

        var crSprite = getCashRegisterSprite(register);

        layoutCashRegister(crSprite, clients);
    }

    private ClientSprite getClientSprite(Client client) {
        return getClientSprite(client.getId());
    }

    private CashRegisterSprite getCashRegisterSprite(CashRegister register) {
        return getCashRegisterSprite(register.getId());
    }

    private void layoutCashRegister(CashRegisterSprite register, List<ClientSprite> clients) {
        int dirX = 0, dirY = 0;

        int deltaX = Math.abs(register.getX() - CENTER_X);
        int deltaY = Math.abs(register.getY() - CENTER_Y);

        if (deltaX > deltaY) {
            dirX = register.getX() > CENTER_X ? -1 : 1;
        } else {
            dirY = register.getY() > CENTER_Y ? -1 : 1;
        }

        int stepX = SPRITE_SIZE + SPRITE_SPACING_X;
        int stepY = SPRITE_SIZE + SPRITE_SPACING_Y;

        int clientX = register.getX() - SPRITE_SPACING_X, clientY = register.getY();

        for (var client : clients) {
            clientX += dirX * stepX;
            clientY += dirY * stepY;

            animController.beginAnimation(client, new Storyboard.Builder()
                    .withDuration(1_000)
                    .withAnimations(new PositionAnimation.Builder()
                            .withBounds(client.getBounds().getLocation(), new Point(clientX, clientY))
                            .withProperty((s, p) -> s.getBounds().setLocation(p))
                            .build())
                    .build());
        }
    }
}
