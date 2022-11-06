package ua.pz33.utils;

import java.math.BigInteger;

public class AmazingStopwatch {
    public static final BigInteger ONE_MILLION = BigInteger.valueOf(1_000_000);


    private BigInteger startTime;
    private BigInteger elapsed;

    public void start() {
        startTime = getCurrentTimeNanos();
    }

    public void stop() {
        var currTime = getCurrentTimeNanos();

        elapsed = currTime.subtract(startTime);

        startTime = null;
    }

    public BigInteger elapsed() {
        return elapsed;
    }

    public long elapsedLong() {
        return elapsed.longValue();
    }

    private static BigInteger getCurrentTimeNanos() {
        var currentMillis = BigInteger.valueOf(System.currentTimeMillis());
        var nanos = currentMillis.multiply(ONE_MILLION);

        return nanos.add(BigInteger.valueOf(System.nanoTime()));
    }
}
