package io.github.inf1009_p10_9.interfaces;

public interface IKeyBindObserves {
    void registerKeyBindObserverTarget(IKeyBindObserverTarget observerTarget);
    void observeKeyBindEvent(IKeyBindEvent keyBindEvent);
}
