package ua.pz33.utils.clock;

public interface PostExecuteObserver extends ClockObserver {
    boolean canBeDeleted();
}
