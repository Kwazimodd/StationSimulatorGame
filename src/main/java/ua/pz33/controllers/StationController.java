package ua.pz33.controllers;

import org.jetbrains.annotations.NotNull;
import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.generators.ClientGenerator;
import ua.pz33.rendering.SpriteRegistry;
import ua.pz33.rendering.animation.AnimationController;
import ua.pz33.rendering.animation.PositionAnimation;
import ua.pz33.rendering.animation.Storyboard;
import ua.pz33.sprites.CashRegisterSprite;
import ua.pz33.sprites.ClientSprite;
import ua.pz33.sprites.Entrance;
import ua.pz33.sprites.Exit;
import ua.pz33.utils.clock.ClockObserver;
import ua.pz33.utils.clock.GameClock;

import java.awt.*;
import java.util.List;
import java.util.*;

public class StationController implements CustomerController, CashRegisterController, ClockObserver {
    private static final int SPRITE_SIZE = 50;
    private static final int SPRITE_SPACING_X = -15;
    private static final int SPRITE_SPACING_Y = 0;
    private static final int CENTER_X = 300;
    private static final int CENTER_Y = 300;

    private final AnimationController animController;

    private final Set<CashRegister> registersToLayoutOnNextTick = new HashSet<>();

    private final Map<Integer, CashRegister> cashRegisters = new HashMap<>();
    private final Map<Integer, CashRegisterSprite> cashRegisterSprites = new HashMap<>();
    private final Map<Integer, CashRegister> backupCashRegisters = new HashMap<>();
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
        GameClock.getInstance().addObserver(this);
    }

    @Override
    public void notifyCashRegisterOpened(CashRegister register) {
        var sprite = getCashRegisterSprite(register);

        sprite.setOpened();
    }

    public void notifyCashRegisterClosed(CashRegister register) {
        var sprite = getCashRegisterSprite(register);

        sprite.setClosed();

        var clientsQueue = register.getClientsQueue();

        if (clientsQueue.isEmpty()) {
            return;
        }

        // Find backup cash register with minimum queue size.
        Optional<CashRegister> bestBackupCashRegister = backupCashRegisters.values().stream()
                .filter(r -> r != register)         // skip current backup register, reference equals is required
                .min(Comparator.comparingInt(cashRegister -> cashRegister.getClientsQueue().size()));

        if (bestBackupCashRegister.isEmpty()) {
            clientsQueue.forEach(client -> client.tryChooseCashRegister(cashRegisters.values()));
        } else {
            bestBackupCashRegister.get().open();
            bestBackupCashRegister.get().tryAddAllToQueue(clientsQueue.stream().toList());
        }
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

    public Client getClient(int id) {
        return clients.get(id);
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

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegister200X200.png", "CashRegisterBroken200X200.png");
        cashRegisterSprite.setBounds(new Rectangle(x, y, 50, 50));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.put(cashRegisterSprite.getId(), cashRegisterSprite);
    }

    public void addBackupCashRegister(int x, int y) {
        CashRegister cashRegister = new CashRegister();
        cashRegister.makeBackup();
        backupCashRegisters.put(cashRegister.getId(), cashRegister);
        GameClock.getInstance().addObserver(cashRegister);

        CashRegisterSprite cashRegisterSprite = new CashRegisterSprite(cashRegister.getId(), "CashRegisterReserved200X200.png", "CashRegisterReserved200X200.png");
        cashRegisterSprite.setBounds(new Rectangle(x, y, 50, 50));
        SpriteRegistry.getInstance().registerSprite(cashRegisterSprite);
        cashRegisterSprites.put(cashRegister.getId(), cashRegisterSprite);
    }

    private void removeClient(int clientId) {
        var clientSprite = clientSprites.get(clientId);
        if (clientSprite == null) {
            System.err.println("Client sprite is null");

            return;
        }
        SpriteRegistry.getInstance().removeSprite(clientSprite);

        clientSprites.remove(clientSprite.getId());
        clients.remove(clientId);

        ClientGenerator.getInstance().clientCount--;
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

    public CashRegister getBackupCashRegister(int id) {
        return backupCashRegisters.get(id);
    }

    @Override
    public boolean hasAnyOpenRegisters() {
        return cashRegisters.values().stream().anyMatch(CashRegister::isOpen);
    }

    @Override
    public void notifyClientBeganService(Client currentClient) {
        currentClient.onStartedService();
    }

    @Override
    public void notifyClientServiced(Client currentClient) {
        currentClient.onFinishedService();
    }

    public CashRegisterSprite getCashRegisterSprite(int id) {
        return cashRegisterSprites.get(id);
    }

    public CashRegisterSprite getCashRegisterSprite(CashRegister register) {
        return cashRegisterSprites.get(register.getId());
    }

    public void notifyQueueUpdated(CashRegister register) {
        registersToLayoutOnNextTick.add(register);
    }

    @NotNull
    private ArrayList<ClientSprite> getQueueCopy(PriorityQueue<Client> clientsQueue) {
        var duplicate = new PriorityQueue<Client>(clientsQueue.comparator());
        duplicate.addAll(clientsQueue);

        var clients = new ArrayList<ClientSprite>();
        for (Client c = duplicate.poll(); c != null; c = duplicate.poll()) {
            clients.add(getClientSprite(c));
        }
        return clients;
    }

    private ClientSprite getClientSprite(Client client) {
        return getClientSprite(client.getId());
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

        int clientX = register.getX() - SPRITE_SPACING_X * dirX;
        int clientY = register.getY() - SPRITE_SPACING_X * dirY;

        for (var client : clients) {
            clientX += dirX * stepX;
            clientY += dirY * stepY;

            Point src = client.getBounds().getLocation();
            Point dest = new Point(clientX, clientY);

            if (src.equals(dest)) {
                continue;
            }

            animController.beginAnimation(client, new Storyboard.Builder()
                    .withDuration(600)
                    .withAnimations(new PositionAnimation.Builder()
                            .withBounds(src, dest)
                            .withProperty((s, p) -> s.getBounds().setLocation(p))
                            .build())
                    .build());
        }
    }

    @Override
    public void onClientServiced(final Client client) {
        var exit = getExit();
        var clientId = client.getId();

        var sprite = getClientSprite(client);

        animController.beginAnimation(sprite, new Storyboard.Builder()
                .withDuration(800)
                .withAnimations(new PositionAnimation.Builder()
                        .withBounds(sprite.getBounds().getLocation(), exit.getBounds().getLocation())
                        .withProperty((s, p) -> s.getBounds().setLocation(p))
                        .build())
                .addOnExecutedListener(() -> removeClient(clientId))
                .build());
    }

    @Override
    public void onTick() {
        layoutCashRegisters();
    }

    private void layoutCashRegisters() {
        if (registersToLayoutOnNextTick.isEmpty()) {
            return;
        }

        for (CashRegister register : registersToLayoutOnNextTick) {
            ArrayList<ClientSprite> clients = getQueueCopy(register.getClientsQueue());

            var crSprite = getCashRegisterSprite(register);

            layoutCashRegister(crSprite, clients);
        }

        registersToLayoutOnNextTick.clear();
    }
}
