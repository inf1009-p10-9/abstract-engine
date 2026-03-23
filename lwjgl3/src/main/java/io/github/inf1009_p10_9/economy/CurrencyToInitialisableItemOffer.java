package io.github.inf1009_p10_9.economy;

public class CurrencyToInitialisableItemOffer<IT extends Object,
                                              DT extends Object,
                                              SWT extends ICurrencyWallet,
                                              TWT extends IItemsWallet<IT>>
    extends CurrencyToItemOffer<IT,DT,SWT,TWT,CurrencyToInitialisableItemOffer<IT,DT,SWT,TWT>> {
    private Class<IT> targetItemType;

    public CurrencyToInitialisableItemOffer(Class<SWT> sourceWalletType,
                                            float sourcePrice,
                                            Class<TWT> targetWalletType,
                                            Class<IT> targetItemType,
                                            DT targetDescriptor,
                                            Integer minQty,
                                            Integer maxQty) {
        super(sourceWalletType,
              sourcePrice,
              targetWalletType,
              targetDescriptor,
              minQty,
              maxQty);
        this.targetItemType = targetItemType;
    }

	@Override
	protected IT createTargetItem() throws Exception {
        return targetItemType.getDeclaredConstructor().newInstance();
	}
}
