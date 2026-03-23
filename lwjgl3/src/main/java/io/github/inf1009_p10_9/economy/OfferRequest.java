package io.github.inf1009_p10_9.economy;

abstract class OfferRequest<OT extends IOfferReadOnly>
        implements IOfferRequest<OT> {
    private OT offer;

    protected OfferRequest(OT offer) {
        this.offer = offer;
    }

    public OT getOffer() {
        return offer;
    }
}

class ItemOfferRequest<OT extends IOfferReadOnly>
        extends OfferRequest<OT>
        implements IItemOfferRequest<OT> {
    private int qty;

    public ItemOfferRequest(OT offer, int qty) {
        super(offer);
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }
}
