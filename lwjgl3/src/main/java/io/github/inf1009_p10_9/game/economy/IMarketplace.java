package io.github.inf1009_p10_9.game.economy;

import java.util.List;

interface IMarketplace {
    /**
       Get a list of valid offers.

       The offers returned are tested valid without consideration of the
       {@link IWalletBagImmutableOwnership} to be used. Hence, an
       {@link IOfferRequest} should be created and passed to
       {@link IOfferReadOnly#isTransactionViable(IWalletBagImmutableOwnership, IOfferRequest)}
       for each relevant offer before presenting it to the user.

       @return Valid offers at point of query.
    */
    List<IOfferReadOnly> getOffers();
    /**
       Redeem an {@link IOfferRequest} against an
       {@link IWalletBagImmutableOwnership}.

       The {@link IOfferRequest} will be validated against the marketplace's
       list of currently-valid offers before attempting to execute the
       transactions with the wallets.

       Note that a {@link IMarketplace} does not care what _kind_ of
       {@link IOfferRequest} is being passed - That's the responsibility of the
       {@link IOfferReadOnly} to verify. At most, the {@link IMarketplace}
       _should_ verify that the offer contained within the {@link IOfferRequest}
       still exists internally (e.g. it's not a stale offer).

       Usually, offers will have a function to create an offer request. For
       example: {@link ITargetItemOfferReadOnly#createOfferRequest(int)}.

       @param <T> The type of {@link IOfferRequest} to redeem.
       @param wallets The wallet bag to redeem the offer request against.
       @param request The offer request to redeem.
       @returns true if the transactions against the wallets are successful.
    */
    <T extends IOfferRequest<?>> boolean redeemOffer(IWalletBagImmutableOwnership wallets, T request);
}
