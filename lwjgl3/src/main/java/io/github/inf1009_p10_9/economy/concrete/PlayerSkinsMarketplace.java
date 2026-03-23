package io.github.inf1009_p10_9.economy.concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.inf1009_p10_9.economy.CurrencyToPreinitialisedItemOffer;
import io.github.inf1009_p10_9.economy.StaticMarketplace;

public class PlayerSkinsMarketplace extends StaticMarketplace {
    private static class PlayerSkinOfferDefinition {
        private final String name;
        private final String texturePath;
        private final float unitPrice;

        public PlayerSkinOfferDefinition(String name,
                                         String texturePath,
                                         float unitPrice) {
            this.name = name;
            this.texturePath = texturePath;
            this.unitPrice = unitPrice;
        }
    }

    private static List<PlayerSkinOfferDefinition>  playerSkinOfferDefinitions = Arrays.asList(new PlayerSkinOfferDefinition("Stripe Racer","cars/car1_stripe_racer.png",100),
                                                                                               new PlayerSkinOfferDefinition("Gray Supercar","cars/car2_gray_supercar.png",300),
                                                                                               new PlayerSkinOfferDefinition("Dark Coupe","cars/car3_dark_coupe.png",500),
                                                                                               new PlayerSkinOfferDefinition("Yellow Supercar","cars/car4_yellow_supercar.png",100),
                                                                                               new PlayerSkinOfferDefinition("White Widebody","cars/car5_white_widebody.png",100),
                                                                                               new PlayerSkinOfferDefinition("Gray Lowrider","cars/car6_gray_lowrider.png",100),
                                                                                               new PlayerSkinOfferDefinition("Orange Hypercar","cars/car7_orange_hypercar.png",100),
                                                                                               new PlayerSkinOfferDefinition("Blue Supercar","cars/car8_blue_supercar.",100));

    public PlayerSkinsMarketplace() {
        super(PlayerSkinsMarketplace.playerSkinOfferDefinitions
              .stream()
              .map(definition -> {
                      PlayerSkin playerSkin = new PlayerSkin(definition.name, definition.texturePath);
                      return new CurrencyToPreinitialisedItemOffer<PlayerSkin,
                                                                   PlayerSkin,
                                                                   CoinsWallet,
                                                                   PlayerSkinsWallet>(CoinsWallet.class,
                                                                                      definition.unitPrice,
                                                                                      PlayerSkinsWallet.class,
                                                                                      playerSkin,
                                                                                      playerSkin,
                                                                                      1,
                                                                                      1);
            })
            // Explicit method-level generics definition due to Java 8's weaker type inference system.
            // This wouldn't be necessary in Java 9+.
            // see: https://stackoverflow.com/questions/37214692/why-does-this-java-8-stream-operation-evaluate-to-object-instead-of-listobject
            // see: https://openjdk.org/jeps/218
            // see: https://chatgpt.com/s/t_69c37cc7e57881919602d4cc39ea2b8a
            //      prompt: Using `.collect(Collectors.toList())` causes the error "incompatible types: inference variable T has incompatible bounds". However, this issue does not persist in Java 21. Identify and explain what change is causing this change of behaviour, and the remedy when sticking to Java 8.
            .collect(Collectors.<CurrencyToPreinitialisedItemOffer<PlayerSkin,
                                                                   PlayerSkin,
                                                                   CoinsWallet,
                                                                   PlayerSkinsWallet>>toList()));
    }
}
