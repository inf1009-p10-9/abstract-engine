package io.github.inf1009_p10_9.economy;

public interface IOfferCurrencyDescriptor {
    Class<? extends ICurrencyWallet> getWalletType();
    float getAmount();
}
