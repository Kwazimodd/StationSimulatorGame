package ua.pz33.generators;

import ua.pz33.utils.configuration.PropertyRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;


public class ClientGenerator
{
    private final Point position;
    public Timer timer;
    private boolean isPaused = false;
    private final int maxAmountOfClients;
    public int clientCount = 0;

    public ClientGenerator()
    {
        position = PropertyRegistry.ENTRANCE_POSITION;
        maxAmountOfClients = PropertyRegistry.MAX_AMOUNT_OF_CLIENTS;
        timer = new Timer(PropertyRegistry.CLIENT_SPAWN_RATE*1000,this::SpawnClient);
        timer.start();
    }

    public void SpawnClient(ActionEvent event)
    {
        if (!isPaused)
        {
            int a = new Random().nextInt(10);
            if (a < 5)
            {
                //50%
                //spawn normal client in pos (x-100,y-100)
            }
            else if (a < 7)
            {
                //20%
                //spawn exempt client in pos (x-100,y-100)
            }
            else if (a<9)
            {
                //20%
                //spawn client with children in pos (x-100,y-100)
            }
            else
            {
                //10%
                //spawn VIP client in pos (x-100,y-100)
            }

            clientCount++;
        }

        if (clientCount >= maxAmountOfClients)
        {
            isPaused = true;
        }

        if (isPaused && clientCount < maxAmountOfClients / 70)
        {
            isPaused = false;
        }
    }

    public void UpdateSpawnRate()
    {
        timer = new Timer(PropertyRegistry.CLIENT_SPAWN_RATE * 1000,this::SpawnClient);
        timer.start();
    }
}
