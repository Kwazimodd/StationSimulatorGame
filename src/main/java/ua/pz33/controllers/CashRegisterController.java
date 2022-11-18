package ua.pz33.controllers;

import ua.pz33.cashregisters.CashRegister;
import ua.pz33.clients.Client;
import ua.pz33.sprites.ImageSprite;

public interface CashRegisterController {
    boolean hasAnyOpenControllers();

    void notifyClientBeganService(Client currentClient);

    ImageSprite getCashRegisterSprite(int id);

    void notifyCashRegisterClosed(CashRegister clientsQueue);

    void notifyQueueUpdated(CashRegister register);

    void notifyClientServiced(Client currentClient);
}
