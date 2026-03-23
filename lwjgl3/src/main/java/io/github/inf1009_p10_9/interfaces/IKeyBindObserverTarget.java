package io.github.inf1009_p10_9.interfaces;

public interface IKeyBindObserverTarget {
    /**
     * Registers an object which to be notified of a keybinding event.
     *
     * @param keyBindEvent The event of interest to subscribe to
     * @param keyBindObserver The object to be notified of the event
     */
    void registerKeyBindObserver(IKeyBindObserves  keyBindObserver, IKeyBindEvent keyBindEvent);
    void deregisterAllKeyBindObservers();
    void deregisterKeyBindObserver(IKeyBindObserves keyBindObserver);
    void deregisterKeyBindObserver(IKeyBindObserves keyBindObserver, IKeyBindEvent keyBindEvent);
}
