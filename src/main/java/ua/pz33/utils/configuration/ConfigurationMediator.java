package ua.pz33.utils.configuration;

import org.intellij.lang.annotations.MagicConstant;

import java.lang.ref.WeakReference;
import java.util.*;

public class ConfigurationMediator {
    private static ConfigurationMediator instance;

    public static ConfigurationMediator getInstance() {
        if (instance == null) {
            instance = new ConfigurationMediator();
        }

        return instance;
    }

    private final List<WeakReference<ConfigurationListener>> listeners = new ArrayList<>();
    private final Map<String, Object> properties = new HashMap<>();

    public void addListener(ConfigurationListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    public void setValue(@MagicConstant(valuesFromClass = PropertyRegistry.class) String property, Object newValue) {
        var oldValue = properties.getOrDefault(property, null);

        if (Objects.equals(newValue, oldValue)) {
            return;
        }

        properties.put(property, newValue);

        for (var weakListenerReference : listeners) {
            var listener = weakListenerReference.get();

            if (listener == null) {
                listeners.remove(weakListenerReference);

                continue;
            }

            listener.onPropertyChanged(new PropertyChangedEventArgs(property, oldValue, newValue));
        }
    }

    public Object getValue(@MagicConstant(valuesFromClass = PropertyRegistry.class) String property) {
        return properties.get(property);
    }

    public Object getValueOrDefault(@MagicConstant(valuesFromClass = PropertyRegistry.class) String property, Object defaultValue) {
        if (!properties.containsKey(property)) {
            setValue(property, defaultValue);
        }

        return getValue(property);
    }
}
