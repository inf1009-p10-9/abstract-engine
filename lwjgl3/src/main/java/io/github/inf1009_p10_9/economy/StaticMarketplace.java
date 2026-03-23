package io.github.inf1009_p10_9.economy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
   A self-contained marketplace which manages an internal list of offers which can be infinitely redeemed.
*/
public abstract class StaticMarketplace implements IMarketplace {
    private List<IOfferExecutable> offers = new ArrayList<>();

    protected StaticMarketplace(Collection<? extends IOfferExecutable> offers) {
        this.offers.addAll(offers);
    }

    public List<IOfferReadOnly> getOffers() {
        return Collections.unmodifiableList(offers);
    }

    @Override
    public <T extends IOfferRequest<?>> boolean redeemOffer(IWalletBagImmutableOwnership wallets, T request) {
        // Re-fetch the offer from the marketplace offers list to block stale offers.
        IOfferExecutable executableOffer = offers.get(offers.indexOf(request.getOffer()));
        if (executableOffer == null)
            return false;
        return executableOffer.initiateTransaction(wallets, request);
    }
}
