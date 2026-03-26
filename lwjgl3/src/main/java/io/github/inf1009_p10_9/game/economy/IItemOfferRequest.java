package io.github.inf1009_p10_9.game.economy;

/**
   An OfferRequest whose lot size reference is the target item qty.
 */
public interface IItemOfferRequest<OT extends IOfferReadOnly>
        extends IOfferRequest<OT> {
    int getQty();
}
