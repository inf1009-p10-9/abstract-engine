package io.github.inf1009_p10_9.economy;

import java.util.List;

interface IMarketplace {
    /**
       Gets a list of offers that are valid at this point in time.

       The offers' validity is without consideration of any WalletBag. Hence, Offer.isTransactionViable() should be called with the relevant WalletBag before presenting each offer.
    */
    List<IOfferReadOnly> getOffers();
    /**
       Redeem an offer.

       Note that a Marketplace does not care what _kind_ of OfferRequest is being passed - That's the responsibility of the Offer to verify.

       At most, the Marketplace _should_ verify that the offer contained within the OfferRequest still exists internally (i.e. it's not a stale offer).
    */
    <T extends IOfferRequest<?>> boolean redeemOffer(IWalletBagImmutableOwnership wallets, T request);
}
