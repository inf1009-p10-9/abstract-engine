package io.github.inf1009_p10_9.game.economy;

/**
   A bag of wallets, where each wallet accepts a certain currency or collection of items.
n
   This delegation of "balance management" to wallets gives flexibility in introducing new sources and targets for barter and trade.

   For both addWallet and removeWallet, IWallet is used instead of IWalletImmutableOwnership as it is expected that the caller is the original creator of the wallets (composition) or is passed the original wallets from the original owner (association).

   It follows that only the owners and prospective owners of the wallets are expected to posses IWalletBag and IWallet.
*/
public interface IWalletBag extends IWalletBagImmutableOwnership {
    /**
       Adds a wallet to the bag if it doesn't already exist.

       Returns true if successful, otherwise false.
    */
    boolean addWallet(IWallet wallet);

    /**
       Removes a wallet from the bag if it exists.

       Returns true if successful, otherwise false.
    */
    boolean removeWallet(IWallet wallet);
}
