package ua.pz33.utils.logs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LogMediator {
    private final List<WeakReference<LogDestination>> destinations = new ArrayList<>();

    private static LogMediator instance;

    public static LogMediator getInstance() {
        if (instance == null) {
            instance = new LogMediator();
        }

        return instance;
    }

    public void addDestination(LogDestination destination) {
        this.destinations.add(new WeakReference<>(destination));
    }

    public void logMessage(String message) {
        if (destinations.isEmpty()) {
            return;
        }

        for (var weakDestinationReference : destinations) {
            var logDestination = weakDestinationReference.get();

            if (logDestination == null) {
                destinations.remove(weakDestinationReference);

                continue;
            }

            logDestination.logMessage(message);
        }
    }
}
