package io.github.inf1009_p10_9.interfaces;

// base contract for all managers, covering setup, per-frame updates, and cleanup
public interface IManager {
    void initialize();
    void update();
    void clear();
}
