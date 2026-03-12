// abandoned attempt at a generic singleton base class for all managers.
// the implementation was incomplete since java does not support static generic fields
// in a way that works cleanly for the singleton pattern, so each manager
// defines its own getInstance() instead.

/*package io.github.inf1009_p10_9.managers;

import io.github.inf1009_p10_9.interfaces.IManager;

public abstract class AbstractManager implements IManager {
    private static T instance;
    private AbstractManager() {}
    public static <T> T getInstance() {
        return
    }
}
*/
