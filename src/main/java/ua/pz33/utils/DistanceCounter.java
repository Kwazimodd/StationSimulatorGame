package ua.pz33.utils;

public class DistanceCounter {
    public static int getDistance(int x1, int y1, int x2, int y2){
        int deltaX = x2 - x1, deltaY = y2 - y1;
        int distance = (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        return distance;
    }
}
