package io.github.inf1009_p10_9.game.economy;

public class CurrencyToPreinitialisedItemOffer<IT extends Object,
                                               DT extends Object,
                                               SWT extends ICurrencyWallet,
                                               TWT extends IItemsWallet<IT>>
    extends CurrencyToItemOffer<IT,DT,SWT,TWT,CurrencyToPreinitialisedItemOffer<IT,DT,SWT,TWT>> {
    private IT targetItem;

    public CurrencyToPreinitialisedItemOffer(Class<SWT> sourceWalletType,
                                             float sourcePrice,
                                             Class<TWT> targetWalletType,
                                             IT targetItem,
                                             DT targetDescriptor,
                                             Integer minQty,
                                             Integer maxQty) {
        super(sourceWalletType,
              sourcePrice,
              targetWalletType,
              targetDescriptor,
              minQty,
              maxQty);
        this.targetItem = targetItem;
    }

	@Override
	protected IT createTargetItem() throws Exception {
        return targetItem;
	}
}
