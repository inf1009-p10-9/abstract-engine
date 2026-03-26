package io.github.inf1009_p10_9.game.economy;

import java.util.List;

interface IWallet extends IWalletImmutableOwnership {
    boolean isClaimableByBag(IWalletBag bag);
    boolean isClaimableByBag(IWalletBag targetBag, IWalletBag currentBag);
    boolean claimOwnership(IWalletBag bag);
    boolean releaseOwnership(IWalletBag bag);
}

interface IWalletImmutableOwnership {}

abstract class Wallet implements IWallet {
    private IWalletBag parentBag;

    @Override
    public boolean isClaimableByBag(IWalletBag bag) {
        if (parentBag != null)
            return false;
        return true;
    }

    @Override
    public boolean isClaimableByBag(IWalletBag targetBag, IWalletBag currentBag) {
        return parentBag == currentBag;
    }

    @Override
    public boolean claimOwnership(IWalletBag bag) {
        if (!isClaimableByBag(bag))
            return false;
        parentBag = bag;
        return true;
    }

    @Override
    public boolean releaseOwnership(IWalletBag bag) {
        if (parentBag != bag)
            return false;
        parentBag = null;
        return true;
    }
}

interface ICurrencyWallet extends IWallet {
    boolean creditBalance(float amount);
    boolean debitBalance(float amount);
    boolean isDebitViable(float amount);
    float getBalance();
}

interface IItemsWallet<T> extends IWallet {
    boolean isItemAcceptable(T item);
    boolean addItem(T item);
    boolean isItemRemovable(T item);
    boolean removeItem(T item);
    List<T> getItems();
}
