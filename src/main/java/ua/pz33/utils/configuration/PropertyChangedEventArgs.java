package ua.pz33.utils.configuration;

import org.intellij.lang.annotations.MagicConstant;

public class PropertyChangedEventArgs {
    private final String propertyName;
    private final Object oldValue, newValue;

    public PropertyChangedEventArgs(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @MagicConstant(valuesFromClass = PropertyRegistry.class)
    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public String getOldValueString() {
        return (String) oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public String getNewValueString() {
        return (String) newValue;
    }
}
