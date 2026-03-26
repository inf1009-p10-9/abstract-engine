package io.github.inf1009_p10_9.game.economy;

import java.util.List;

public interface IWalletBagImmutableOwnership {
    /**
       An immutable list of wallets stored inside the bag.
    */
    <T extends IWalletImmutableOwnership> List<T> getWallets(Class<T> walletType);
}
