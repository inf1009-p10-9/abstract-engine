package io.github.inf1009_p10_9;

import io.github.inf1009_p10_9.economy.IWalletBag;
import io.github.inf1009_p10_9.economy.IWalletBagImmutableOwnership;
import io.github.inf1009_p10_9.economy.WalletBag;
import io.github.inf1009_p10_9.economy.concrete.CoinsWallet;
import io.github.inf1009_p10_9.economy.concrete.PlayerSkin;
import io.github.inf1009_p10_9.economy.concrete.PlayerSkinsWallet;

public class PlayerState {
    private static PlayerState instance;

    private final IWalletBag wallets = new WalletBag();
    // Create an identical PlayerSkin from the Marketplace to demonstrate ItemWallet's enforcement of item uniqueness.
    // We can create a new instance here due to PlayerSkin's equality check supporting fungibility.
    private PlayerSkin activePlayerSkin = new PlayerSkin("Stripe Racer","cars/car1_stripe_racer.png");

    private PlayerState() {
        CoinsWallet coinsWallet = new CoinsWallet();
        PlayerSkinsWallet playerSkinsWallet = new PlayerSkinsWallet();
        coinsWallet.creditBalance(300);
        playerSkinsWallet.addItem(activePlayerSkin);

        wallets.addWallet(coinsWallet);
        wallets.addWallet(playerSkinsWallet);
    }

    public static PlayerState getInstance() {
        if (instance == null)
            instance = new PlayerState();
        return instance;
    }

    public IWalletBagImmutableOwnership getWalletBag() {
        return wallets;
    }

    public PlayerSkin getActivePlayerSkin() {
        return activePlayerSkin;
    }

    public void setActivePlayerSkin(PlayerSkin playerSkin) {
        activePlayerSkin = playerSkin;
    }
}
