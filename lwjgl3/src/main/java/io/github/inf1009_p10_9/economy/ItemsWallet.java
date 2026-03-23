package io.github.inf1009_p10_9.economy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ItemsWallet<T> extends Wallet implements IItemsWallet<T> {
    private final List<T> items = new ArrayList<>();

    @Override
    public boolean isItemAcceptable(T item) {
        for (T walletItem : items) {
            if (walletItem.equals(item))
                return false;
        }
        return true;
    }

    @Override
    public boolean addItem(T item) {
        if (!isItemAcceptable(item))
            return false;
        return items.add(item);
    }

    @Override
    public boolean isItemRemovable(T item) {
        return items.contains(item);
    }

    @Override
    public boolean removeItem(T item) {
        if (!isItemRemovable(item))
            return false;
        return items.remove(item);
    }

    @Override
    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }
}
