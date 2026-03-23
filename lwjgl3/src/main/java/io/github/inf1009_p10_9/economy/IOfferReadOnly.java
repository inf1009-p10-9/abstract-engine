package io.github.inf1009_p10_9.economy;

public interface IOfferReadOnly {
    <T extends IOfferRequest<?>> boolean isTransactionViable(IWalletBagImmutableOwnership wallets, T request);
}
