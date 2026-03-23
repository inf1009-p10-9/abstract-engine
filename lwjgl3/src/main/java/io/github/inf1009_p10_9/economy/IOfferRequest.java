package io.github.inf1009_p10_9.economy;

public interface IOfferRequest<OT extends IOfferReadOnly> {
    OT getOffer();
}
