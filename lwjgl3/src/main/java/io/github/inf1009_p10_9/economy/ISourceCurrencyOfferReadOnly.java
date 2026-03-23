package io.github.inf1009_p10_9.economy;

public interface ISourceCurrencyOfferReadOnly {
    /**
       Retrieve the arbitrary item descriptor for use before purchase (e.g. to display to the user).

       Unlike the descriptor for items, this is standardised to return the wallet type and amount.
    */
    IOfferCurrencyDescriptor getSourceCurrencyDescriptor();
}
