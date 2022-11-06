package ua.pz33.generators;

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

    public ClientGenerator(int x, int y, int spawnRateInSeconds, int maxAmount)
    {
        position = new Point(x,y);
        maxAmountOfClients = maxAmount;
        timer = new Timer(spawnRateInSeconds*1000,this::SpawnClient);
        timer.start();
    }

    public ClientGenerator(int x, int y, int maxAmount)
    {
        position = new Point(x,y);
        maxAmountOfClients = maxAmount;
        timer = new Timer(1000,this::SpawnClient);
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

    public void ChangeSpawnRate(int newSpawnRate)
    {
        timer = new Timer(newSpawnRate*1000,this::SpawnClient);
        timer.start();
    }
}
