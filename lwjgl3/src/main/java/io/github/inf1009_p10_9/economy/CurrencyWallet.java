package io.github.inf1009_p10_9.economy;

public abstract class CurrencyWallet extends Wallet implements ICurrencyWallet {
    protected final String denomination;
    protected float balance;

    protected CurrencyWallet(String denomination) {
        this.denomination = denomination;
    }

    @Override
    public boolean creditBalance(float amount) {
        if (amount < 0)
            return false;
        balance += amount;
        return true;
    }

    @Override
    public boolean debitBalance(float amount) {
        if (!isDebitViable(amount))
            return false;
        balance -= amount;
        return true;
    }

    @Override
    public boolean isDebitViable(float amount) {
        return amount > 0 && amount <= balance;
    }

    @Override
    public float getBalance() {
        return balance;
    }
}
