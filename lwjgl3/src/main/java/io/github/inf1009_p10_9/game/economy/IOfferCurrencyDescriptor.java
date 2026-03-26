package io.github.inf1009_p10_9.game.economy;

/**
   A type-safe description of the Offer's source/target currency.
 */
public interface IOfferCurrencyDescriptor {
    Class<? extends ICurrencyWallet> getWalletType();
    float getAmount();
}
