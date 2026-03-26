package io.github.inf1009_p10_9.game.economy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WalletBag implements IWalletBag {
    private List<IWallet> wallets = new ArrayList<>();

    public <T extends IWalletImmutableOwnership> List<T> getWallets(Class<T> walletType) {
        return wallets.stream()
            .filter(walletType::isInstance)
            .map(walletType::cast)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                                                  Collections::unmodifiableList));
    }

    @Override
    public boolean addWallet(IWallet wallet) {
        if (wallets.contains(wallet))
            return false;
        if (!wallet.claimOwnership(this))
            return false;
        ;
        return wallets.add(wallet);
    }

    @Override
    public boolean removeWallet(IWallet wallet) {
        return wallets.remove(wallet);
    }
}
