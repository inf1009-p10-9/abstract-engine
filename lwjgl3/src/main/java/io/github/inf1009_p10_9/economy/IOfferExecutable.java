package io.github.inf1009_p10_9.economy;

public interface IOfferExecutable extends IOfferReadOnly {
    <T extends IOfferRequest<?>> boolean initiateTransaction(IWalletBagImmutableOwnership wallets, T request);
}
