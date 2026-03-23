package io.github.inf1009_p10_9.economy;

/**
   Implemented by an Offers which will debit an Item into the target wallet.
*/
public interface ITargetItemOfferReadOnly<DT extends Object,
                                          OT extends IOfferReadOnly>
        extends IOfferReadOnly {
    /**
       Retrieve the arbitrary item descriptor for use before purchase (e.g. to display to the user).

       The descriptor would usually contain stats and descriptive elements about the item.
    */
    DT getTargetItemDescriptor();
    /**
       Create an OfferRequest which can be passed to the originating Marketplace to execute the transaction.
    */
    <RT extends IItemOfferRequest<OT>> RT createOfferRequest(int qty);
}
