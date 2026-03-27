package io.github.inf1009_p10_9.game.economy;

/**
   Default implementation of {@link IOfferCurrencyDescriptor}.

   @see IOfferCurrencyDescriptor
 */
class OfferCurrencyDescriptor implements IOfferCurrencyDescriptor {
    private Class<? extends ICurrencyWallet> walletType;
    private float amount;

    /**
       @param walletType Wallet class to accept.
       @param float Transaction amount.
     */
    public OfferCurrencyDescriptor(Class<? extends ICurrencyWallet> walletType,
                                   float amount) {
        this.walletType = walletType;
        this.amount = amount;
    }

    /**
       @see IOfferCurrencyDescriptor#getWalletType()
    */
	@Override
	public Class<? extends ICurrencyWallet> getWalletType() {
        return walletType;
	}

    /**
       @see IOfferCurrencyDescriptor#getAmount()
    */
	@Override
	public float getAmount() {
        return amount;
	}
}

abstract class Offer<SWT extends IWallet,
                     TWT extends IWallet> implements IOfferExecutable {
    protected Class<SWT> sourceWalletType;
    protected Class<TWT> targetWalletType;
}

/**
   An offer which trades debit from a {@link ICurrencyWallet} for an
   object which is accepted by a {@link IItemsWallet}.

   Duplication of types in the generics and constructors is necessary to provide
   compile-time type safety and runtime {@link Object#isInstance} checks
   respectively.

   @param <IT> Target item type to be traded for.
   @param <DT> Descriptor type to represent target item.
   @param <SWT> Source currency wallet type.
   @param <TWT> Target item wallet type.
   @param <OT> Offer type.
 */
abstract class CurrencyToItemOffer<IT extends Object,
                                   DT extends Object,
                                   SWT extends ICurrencyWallet,
                                   TWT extends IItemsWallet<IT>,
                                   OT extends CurrencyToItemOffer<IT,DT,SWT,TWT,OT>>
        extends Offer<SWT,TWT>
        implements ISourceCurrencyOfferReadOnly,
                   ITargetItemOfferReadOnly<DT,OT> {
    protected final float sourceUnitPrice;
    protected final Integer minQty;
    protected final Integer maxQty;
    private final DT targetDescriptor;

    /**
       @param sourceWalletType Source currency wallet class.
       @param sourcePrice Unit price of trade.
       @param targetWalletType Target item wallet class.
       @param targetDescriptor Instance of descriptor to represent item
              pre-purchase.
       @param minQty Minimum quantity of target item to trade. Nullable.
       @param maxQty Maximum quantity of target item to trade. Nullable.
       @throws IllegalArgumentException `minQty <= maxQty` is not satisfied.
     */
    public CurrencyToItemOffer(Class<SWT> sourceWalletType,
                               float sourcePrice,
                               Class<TWT> targetWalletType,
                               DT targetDescriptor,
                               Integer minQty,
                               Integer maxQty) throws IllegalArgumentException {
        this.sourceWalletType = sourceWalletType;
        this.sourceUnitPrice = sourcePrice;
        this.targetWalletType = targetWalletType;
        this.targetDescriptor = targetDescriptor;

        // Check if minQty or maxQty is less than 0
        if ((minQty != null && minQty < 0) || (maxQty != null && maxQty < 0))
            throw new IllegalArgumentException();

        // Check if maxQty < minQty
        if (minQty != null && maxQty != null && maxQty < minQty)
            throw new IllegalArgumentException();

        this.minQty = minQty;
        this.maxQty = maxQty;
    }

    public IOfferCurrencyDescriptor getSourceCurrencyDescriptor() {
        return new OfferCurrencyDescriptor(sourceWalletType, sourceUnitPrice);
    }

    public DT getTargetItemDescriptor() {
        return targetDescriptor;
    }

    abstract protected IT createTargetItem() throws Exception;

    /**
       Returns the first source {@link ICurrencyWallet} which claims the
       transaction is viable.

       @param wallets The wallet bag to check against.
       @param totalPrice Total price of the transaction to check viabiliy for.
     */
    protected SWT getSourceWalletFromBag(IWalletBagImmutableOwnership wallets,
                                         float totalPrice) {
        return
            wallets
            .getWallets(sourceWalletType)
            .stream()
            .peek(x -> System.out.print("."))
            .filter(sourceWalletType::isInstance)
            .peek(x -> System.out.print(","))
            .filter(x -> x.isDebitViable(totalPrice))
            .peek(x -> System.out.print("%"))
            .findFirst()
            .map(x -> x)
            .orElse(null);
    }

    /**
       Returns the first target {@link IItemsWallet} which claims the
       transaction is viable.

       @param wallets The wallet bag to check against.
       @param targetItem Target item to check viability against.
     */
    protected TWT getTargetWalletFromBag(IWalletBagImmutableOwnership wallets,
                                         IT targetItem) {
        return
            wallets
            .getWallets(targetWalletType)
            .stream()
            .peek(x -> System.out.print("!"))
            .filter(targetWalletType::isInstance)
            .peek(x -> System.out.print(","))
            .filter(x -> x.isItemAcceptable(targetItem))
            .peek(x -> System.out.print("%"))
            .findFirst()
            .map(x -> x)
            .orElse(null);
    }

    @Override
    public ItemOfferRequest<OT> createOfferRequest(int qty) {
        // Casting needed due Java's `this` not playing nice with the circular
        // types.
        // This is OK as we are literally inserting the class itself as the
        // Offer into the OfferRequest.
        // Safe to say we know what's inside.
        return new ItemOfferRequest<OT>((OT)this, qty);
    }

    @Override
    public <T extends IOfferRequest<?>> boolean isTransactionViable(IWalletBagImmutableOwnership wallets,
                                                                    T request) {
        // Safety check: Don't allow OfferRequest for other Offer to be used.
        System.out.println("[isTransactionViable] Checking request against wallets...");
        if (request.getOffer() != this)
            return false;
        System.out.println("[isTransactionViable] Request belongs to this Offer.");

        IItemOfferRequest<?> itemRequest;
        if (!(request instanceof IItemOfferRequest))
            return false;
        itemRequest = (IItemOfferRequest<?>)request;
        System.out.println("[isTransactionViable] Request target type is correct.");

        int qty = itemRequest.getQty();
        System.out.printf("[isTransactionViable] qty/min/max: %d/%d/%d\n", qty, minQty, maxQty);
        if ((minQty != null && qty < minQty) || (maxQty != null && qty > maxQty))
            return false;
        System.out.println("[isTransactionViable] Request qty is valid.");

        float totalPrice = sourceUnitPrice * qty;
        SWT sourceWallet = getSourceWalletFromBag(wallets, totalPrice);
        if (sourceWallet == null)
            return false;
        System.out.println("[isTransactionViable] Viable source wallet type found in bag.");

        IT targetItem;
        try {
            targetItem = createTargetItem();
        } catch (Exception e) {
            return false;
        }
        System.out.println("[isTransactionViable] Target item created.");

        TWT targetWallet = getTargetWalletFromBag(wallets, targetItem);
        if (targetWallet == null)
            return false;
        System.out.println("[isTransactionViable] Viable target wallet type found in bag.");

        return true;
    }


	@Override
	public <T extends IOfferRequest<?>> boolean initiateTransaction(IWalletBagImmutableOwnership wallets,
                                                                    T request) {
        if (!isTransactionViable(wallets, request))
            return false;

        IItemOfferRequest<?> itemRequest;
        if (!(request instanceof IItemOfferRequest))
            return false;
        itemRequest = (IItemOfferRequest<?>)request;

        int qty = itemRequest.getQty();
        float totalPrice = sourceUnitPrice * qty;

        SWT sourceWallet = getSourceWalletFromBag(wallets, totalPrice);
        if (!sourceWallet.debitBalance(totalPrice))
            return false;

        IT targetItem;
        try {
            targetItem = createTargetItem();
        } catch (Exception e) {
            sourceWallet.creditBalance(totalPrice);
            return false;
        }

        TWT targetWallet = getTargetWalletFromBag(wallets, targetItem);
        if (!targetWallet.addItem(targetItem)) {
            sourceWallet.creditBalance(totalPrice);
            return false;
        }
        return true;
	}
}
