package io.github.inf1009_p10_9.game.economy;

/**
   A type-safe description of the Offer's source/target currency.

   A descriptor acts as an analogue to either the source or target half of an
   {@link IOfferReadOnly} with the benefit of type down-casting and without
   awareness of the transcation "direction".

   The transaction direction can be deduced by checking if the originating
   {@link IOfferReadOnly} also implements {@link ISourceCurrencyOfferReadOnly}.
 */
public interface IOfferCurrencyDescriptor {
    Class<? extends ICurrencyWallet> getWalletType();
    float getAmount();
}
