/*package io.github.inf1009_p10_9.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface IBalanceDepositable {
    void depositBalance(float amount);
}

interface IBalanceRedeemable {
    float getBalance();
    boolean redeemBalance(float amount);
}

public class Wallet implements IBalanceDepositable, IBalanceRedeemable {
    private float balance = 0;
    private List<StoreFrontItem> items = new ArrayList<>();

    public Wallet(float initialBalance) {
        balance = initialBalance;
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public void depositBalance(float amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be greater than 0");
        balance += amount;
    }

    public List<StoreFrontItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean redeemBalance(StoreFrontItem item) {

    }

    public class StoreFrontItem {
        private String name;
        private float price;
    }
}
*/
