
// Modify PlaeyrSkinOffer and PlayerSkinReesource to bubble up OfferRequest qqqqty
// 1. Check if instanceof ITargetItemOfferReadOnly,
// 2. Call getQty and store result.

package io.github.inf1009_p10_9.scenes;

import io.github.inf1009_p10_9.entities.Entity;
import io.github.inf1009_p10_9.input.KeyBindEvent;
import io.github.inf1009_p10_9.interfaces.ICollidableRegisterable;
import io.github.inf1009_p10_9.interfaces.IEntityRegisterable;
import io.github.inf1009_p10_9.interfaces.IKeyBindEvent;
import io.github.inf1009_p10_9.interfaces.IKeyBindObserverTarget;
import io.github.inf1009_p10_9.interfaces.IKeyBindObserves;
import io.github.inf1009_p10_9.interfaces.IMusicPlayable;
import io.github.inf1009_p10_9.interfaces.IRenderRegisterable;
import io.github.inf1009_p10_9.interfaces.ISceneSwitchable;
import io.github.inf1009_p10_9.interfaces.IUIDisplayable;
import io.github.inf1009_p10_9.ui.FontManager;
import io.github.inf1009_p10_9.ui.SceneBackdrop;
import io.github.inf1009_p10_9.ui.TitleElement;
import io.github.inf1009_p10_9.ui.TextLabel;
import io.github.inf1009_p10_9.PlayerState;
import io.github.inf1009_p10_9.economy.IItemOfferRequest;
import io.github.inf1009_p10_9.economy.IOfferCurrencyDescriptor;
import io.github.inf1009_p10_9.economy.IOfferReadOnly;
import io.github.inf1009_p10_9.economy.ISourceCurrencyOfferReadOnly;
import io.github.inf1009_p10_9.economy.ITargetItemOfferReadOnly;
import io.github.inf1009_p10_9.economy.IWalletBagImmutableOwnership;
import io.github.inf1009_p10_9.economy.concrete.CoinsWallet;
import io.github.inf1009_p10_9.economy.concrete.PlayerSkin;
import io.github.inf1009_p10_9.economy.concrete.PlayerSkinsMarketplace;
import io.github.inf1009_p10_9.economy.concrete.PlayerSkinsWallet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class CustomisationScene extends Scene implements IKeyBindObserves {
    private final ISceneSwitchable sceneSwitchable;
    private final FontManager fontManager;
    private final PlayerState playerState;
    private IKeyBindObserverTarget keyBindObserverTarget;

    private int selectionId = 0;
    private final PlayerSkinsMarketplace playerSkinsMarketplace = new PlayerSkinsMarketplace();
    private final List<PlayerSkinResource> playerSkinResources = new ArrayList<>();
    private final float playerSkinWidth = 52;
    private final float playerSkinHeight = 95;

    // ui elements
    private TitleElement titleElement;
    private TextLabel titleLabel;
    private TextLabel nameLabel;
    private TextLabel balanceLabel;
    private TextLabel priceLabel;
    private SceneBackdrop backdrop;

    // input state
    private long inputDebounce = 0;
    private int inputDebounceDelay = 500;
    private boolean inputDebounceFirst = true;
    private String activeKeybindEvent = null;

    // colors
    private static final Color ACTIVE_ROW_COLOR = new Color(1.0f, 0.55f, 0.0f, 1f); // vibrant orange

    public CustomisationScene(IEntityRegisterable entityRegisterable,
                              IUIDisplayable uiDisplayable,
                              ICollidableRegisterable collidableRegisterable,
                              IRenderRegisterable renderRegisterable,
                              IMusicPlayable musicPlayable,
                              IKeyBindObserverTarget keyBindObserverTarget,
                              ISceneSwitchable sceneSwitchable,
                              FontManager fontManager,
                              PlayerState playerState) {
		super(CustomisationScene.class.getSimpleName(),
              entityRegisterable,
              uiDisplayable,
              collidableRegisterable,
              renderRegisterable,
              musicPlayable);
		this.keyBindObserverTarget = keyBindObserverTarget;
		this.sceneSwitchable = sceneSwitchable;
		this.fontManager = fontManager;
        this.playerState = playerState;
	}

    @Override
    public void load() {
        super.load();
        inputDebounce = 0;
        inputDebounceDelay = 500;
        activeKeybindEvent = null;
        registerKeyBindObserverTarget(this.keyBindObserverTarget);
        if (selectionId < 0)
            selectionId = 0;
        int playerSkinResourcesQty = playerSkinResources.size();
        if (selectionId >= playerSkinResourcesQty)
            selectionId = playerSkinResourcesQty - 1;
    }

    @Override
    public void unload() {
        super.unload();
        keyBindObserverTarget.deregisterKeyBindObserver(this);

        playerSkinResources
            .stream()
            .filter(PlayerSkinResourcePurchasable.class::isInstance)
            .map(x -> ((PlayerSkinResourcePurchasable)x).getEntity())
            .peek(renderRegisterable::unregisterRenderable)
            .peek(x -> x.dispose());
        playerSkinResources.clear();
    }

    private class PlayerSkinOffer {
        private final IItemOfferRequest<?> offerRequest;
        private final IOfferCurrencyDescriptor currencyDescriptor;
        private final PlayerSkin skinDescriptor;
        private final boolean isTransactionViable;

        public PlayerSkinOffer(IItemOfferRequest<?> offerRequest,
                               IOfferCurrencyDescriptor currencyDescriptor,
                               PlayerSkin skinDescriptor,
                               boolean isTransactionViable) {
            this.offerRequest = offerRequest;
            this.currencyDescriptor = currencyDescriptor;
            this.skinDescriptor = skinDescriptor;
            this.isTransactionViable = isTransactionViable;
        }
    }

    private class PlayerSkinResource {
        private final PlayerSkin skinDescriptor;
        private final PlayerSkinEntity entity;

        public PlayerSkinResource(PlayerSkin skinDescriptor,
                                  PlayerSkinEntity entity) {
            this.skinDescriptor = skinDescriptor;
            this.entity = entity;
        }

        public PlayerSkinEntity getEntity() {
            return entity;
        }
    }

    private class PlayerSkinResourcePurchasable extends PlayerSkinResource {
        protected final IOfferCurrencyDescriptor currencyDescriptor;
        private final IItemOfferRequest<?> offerRequest;
        private final boolean isTransactionViable;

        public PlayerSkinResourcePurchasable(PlayerSkinOffer offer,
                                             PlayerSkinEntity entity) {
            super(offer.skinDescriptor,
                  entity);
            this.currencyDescriptor = offer.currencyDescriptor;
            this.offerRequest = offer.offerRequest;
            this.isTransactionViable = offer.isTransactionViable;
        }
    }

    private class PlayerSkinResourcePurchased extends PlayerSkinResource {
        private final PlayerSkin skin;

        public PlayerSkinResourcePurchased(PlayerSkin skin,
                                           PlayerSkinEntity entity) {
            super(skin,
                  entity);
            this.skin = skin;
        }
    }

    private class PlayerSkinEntity extends Entity {
        private Color color = Color.BLUE;

        public PlayerSkinEntity(float x, float y, String texturePath) {
            super(x, y, playerSkinWidth, playerSkinHeight, 10);
            loadTexture(texturePath);
        }

        private void loadTexture(String texturePath) {
            try {
                texture = new Texture(Gdx.files.internal(texturePath));
            } catch (Exception e) {
                System.err.println("Failed to load player texture: " + e.getMessage());
            }
        }

		@Override
		public void renderShapes(ShapeRenderer shapeRenderer) {
            if (texture == null) {
                shapeRenderer.setColor(color);
                shapeRenderer.rect(position.x, position.y, width, height);
            }
        }

		@Override
		public int getCollisionLayer() {
            return 0;
        }

		@Override
		public void update() {}

        //@Override
        public void dispose() {
            if (texture != null)
                texture.dispose();
        }
    }

    private List<PlayerSkinOffer> loadMarketplace() {
        List<PlayerSkinOffer> playerSkinOffers = new ArrayList<>();
        System.out.println("[CustomisationScene] Loading marketplace...");

        // #TODO(RIFA): FIXME - Refactor into constructor injection
        IWalletBagImmutableOwnership wallets = playerState.getWalletBag();

        List<IOfferReadOnly> offers = playerSkinsMarketplace.getOffers();
        System.out.printf("[CustomisationScene] Offers provided: %d", offers.size());
        for (IOfferReadOnly offer : offers) {
            System.out.printf("[CustomisationScene] Testing offer: %s\n", offer.toString());

            // Get offers and ensure they're supported.
            ISourceCurrencyOfferReadOnly sourceCurrencyOffer;
             ITargetItemOfferReadOnly<?,?> targetItemOffer;
            if (!(offer instanceof ISourceCurrencyOfferReadOnly))
                continue;
            System.out.println("[CustomisationScene] Offer source type is valid.");
            sourceCurrencyOffer = (ISourceCurrencyOfferReadOnly)offer;
            if (!(offer instanceof ITargetItemOfferReadOnly))
                continue;
            System.out.println("[CustomisationScene] Offer target type is valid.");
            targetItemOffer = (ITargetItemOfferReadOnly<?,?>)offer;

            // Get source descriptor and ensure they're supported for display to the user.
            IOfferCurrencyDescriptor sourceCurrencyDescriptor = sourceCurrencyOffer.getSourceCurrencyDescriptor();
            if (sourceCurrencyDescriptor.getWalletType() != CoinsWallet.class)
                continue;
            System.out.println("[CustomisationScene] Offer source wallet type is valid.");

            // Get target descriptor and ensure they're supported for display to the user.
            Object targetItemDescriptor = targetItemOffer.getTargetItemDescriptor();
            PlayerSkin skinDescriptor;
            if (!(targetItemDescriptor instanceof PlayerSkin))
                continue;
            System.out.println("[CustomisationScene] Offer target descriptor type is valid.");
            skinDescriptor = (PlayerSkin)targetItemDescriptor;

            // Create an offer request and test if it's viable based on user's balance/s.
            // We allow non-viable transactions to pass through so that we can display them to the user as such.
            IItemOfferRequest<?> offerRequest = targetItemOffer.createOfferRequest(1);
            boolean isTransactionViable = targetItemOffer.isTransactionViable(wallets, offerRequest);
            System.out.printf("[CustomisationScene] Offer is viable with player's wallet? %b\n", isTransactionViable);

            // Compile valid offers
            playerSkinOffers.add(new PlayerSkinOffer(offerRequest,
                                                     sourceCurrencyDescriptor,
                                                     skinDescriptor,
                                                     isTransactionViable));
            System.out.println("[CustomisationScene] Offer accepted!");
        }

        return playerSkinOffers;
    }

  @Override
	protected void loadEntities() {
	    float screenWidth = Gdx.graphics.getWidth();
	    float centerX = screenWidth / 2;
	    float screenHeight = Gdx.graphics.getHeight();
	    float centerY = screenHeight / 2;

	    // shared decorative background
	    backdrop = new SceneBackdrop(true);
	    backdrop.addToScene(this);

	    // title - top left
	    titleElement = new TitleElement("SELECT SKIN", fontManager.getLargeFont(), Color.GREEN);
	    addUI(titleElement);

	    // balance - top right
	    PlayerState playerState = PlayerState.getInstance();
	    String balanceText = "Balance: " + playerState.getWalletBag().getWallets(CoinsWallet.class).get(0).getBalance();
	    balanceLabel = new TextLabel(balanceText, screenWidth - 250, screenHeight - 40, fontManager.getMediumFont());
	    balanceLabel.setColor(ACTIVE_ROW_COLOR);
	    addUI(balanceLabel);

	    // car name - centered below cars
	    nameLabel = new TextLabel("", centerX - 150, centerY - 110, fontManager.getMediumFont());
	    nameLabel.setColor(ACTIVE_ROW_COLOR);
	    addUI(nameLabel);

	    // price or purchased status - below name
	    priceLabel = new TextLabel("", centerX - 150, centerY - 155, fontManager.getMediumFont());
	    priceLabel.setColor(ACTIVE_ROW_COLOR);
	    addUI(priceLabel);

	    // navigation hint - bottom center
	    TextLabel hintLabel = new TextLabel("< > to browse     ENTER to select", centerX, 60, fontManager.getSmallFont());
	    hintLabel.setColor(new Color(1f, 1f, 0.6f, 1f));
	    addUI(hintLabel);

	    List<PlayerSkinOffer> playerSkinOffers = loadMarketplace();
	    PlayerSkinsWallet playerSkinsWallet = playerState.getWalletBag().getWallets(PlayerSkinsWallet.class).get(0);

	    List<PlayerSkin> playerSkinsPurchased = playerSkinsWallet.getItems();
	    int playerSkinsPurchasedQty = playerSkinsPurchased.size();
	    int playerSkinsPurchasableQty = playerSkinOffers.size();
	    int playerSkinsQty = playerSkinsPurchasedQty + playerSkinsPurchasableQty;

	    for (int i = 0; i < playerSkinsPurchasedQty; i++) {
	        PlayerSkin playerSkin = playerSkinsPurchased.get(i);
	        String skinPath = playerSkin.getTexturePath();
	        PlayerSkinEntity playerSkinEntity = new PlayerSkinEntity(
	            centerX - (playerSkinHeight + 10) * (playerSkinsQty / 2 - i),
	            centerY - 16, skinPath);
	        addEntity(playerSkinEntity);
	        renderRegisterable.registerRenderable(playerSkinEntity);
	        playerSkinResources.add(new PlayerSkinResourcePurchased(playerSkin, playerSkinEntity));
	    }

	    for (int i = 0; i < playerSkinsPurchasableQty; i++) {
	        PlayerSkinOffer playerSkinOffer = playerSkinOffers.get(i);
	        PlayerSkin playerSkinDescriptor = playerSkinOffer.skinDescriptor;
	        String skinPath = playerSkinDescriptor.getTexturePath();
	        PlayerSkinEntity playerSkinEntity = new PlayerSkinEntity(
	            centerX - (playerSkinHeight + 10) * (playerSkinsQty / 2 - i),
	            centerY - 16, skinPath);
            if (playerState.getWalletBag().getWallets(PlayerSkinsWallet.class).get(0).isItemAcceptable(playerSkinDescriptor)) {
                addEntity(playerSkinEntity);
                renderRegisterable.registerRenderable(playerSkinEntity);
                playerSkinResources.add(new PlayerSkinResourcePurchasable(playerSkinOffer, playerSkinEntity));
            }
	    }

	    System.out.println("CustomisationScene loaded");
	}

    @Override
    public void update() {
        handleNavigation();
        handleDescriptorDisplay();
        handleRender();
        float delta = Gdx.graphics.getDeltaTime();

        backdrop.update(delta, 1f, 1f);
    }

    // updates arrow and label colors to show which row is active, and refreshes displayed values
    private void handleDescriptorDisplay() {
        PlayerSkinResource highlightedSkinResource;
        try {
            highlightedSkinResource = playerSkinResources.get(selectionId);
        } catch (Exception e) {
            return;
        }

        // update price or purchased status
        PlayerSkinResourcePurchasable highlightedSkinResourcePurchasable;
        if (highlightedSkinResource instanceof PlayerSkinResourcePurchasable) {
            highlightedSkinResourcePurchasable = (PlayerSkinResourcePurchasable)highlightedSkinResource;
            if (highlightedSkinResourcePurchasable.isTransactionViable)
                priceLabel.setText(String.valueOf(highlightedSkinResourcePurchasable.currencyDescriptor.getAmount() *
                                                  highlightedSkinResourcePurchasable.offerRequest.getQty()));
            else
                priceLabel.setText(String.valueOf(highlightedSkinResourcePurchasable.currencyDescriptor.getAmount() *
                                                  highlightedSkinResourcePurchasable.offerRequest.getQty()) + " (not enough money)");
        } else {
            priceLabel.setText("Already purchased.");;
        }

        nameLabel.setText(highlightedSkinResource.skinDescriptor.getDisplayName());

        // update balance
        balanceLabel.setText("Balance: " + playerState.getWalletBag()
            .getWallets(CoinsWallet.class).get(0).getBalance());
    }

	@Override
	public void registerKeyBindObserverTarget(IKeyBindObserverTarget observerTarget) {
        if (observerTarget != null)
            keyBindObserverTarget.deregisterKeyBindObserver(this);
        Arrays
            .asList("MENU_LEFT", "MOVE_LEFT", "MENU_RIGHT", "MOVE_RIGHT", "CONFIRM", "BACK")
            .forEach(keyBind -> {
                    observerTarget.registerKeyBindObserver(this, new KeyBindEvent(keyBind, "KEY_UP"));
                    observerTarget.registerKeyBindObserver(this, new KeyBindEvent(keyBind, "KEY_DOWN"));
        });

        keyBindObserverTarget = observerTarget;
	}

	@Override
	public void observeKeyBindEvent(IKeyBindEvent keyBindEvent) {
        switch (keyBindEvent.getEvent()) {
        case "KEY_DOWN":
            activeKeybindEvent = keyBindEvent.getKeyBind();
            inputDebounce = System.currentTimeMillis();
            inputDebounceFirst = true;
            break;
        case "KEY_UP":
            if (activeKeybindEvent == keyBindEvent.getKeyBind()) {
                activeKeybindEvent = null;
                inputDebounceDelay = 500;
            }
            break;
        }
	}

    private void handleRender() {
        float screenWidth = Gdx.graphics.getWidth();
        float centerX = screenWidth / 2;
        float screenHeight = Gdx.graphics.getHeight();
        float centerY = screenHeight / 2;
        int playerSkinsQty = playerSkinResources.size();
        for (int i = 0; i < playerSkinsQty; i++) {
            PlayerSkinEntity skinEntity = playerSkinResources.get(i).getEntity();
            Vector2 curPos = skinEntity.getPosition();
            curPos.set(centerX - (playerSkinHeight + 10) * (selectionId - i) + 16, centerY);
        }
    }

    private void handleNavigation() {
        if (activeKeybindEvent == null)
            return;
        if (inputDebounceFirst ||  System.currentTimeMillis() - inputDebounce > inputDebounceDelay) {
            switch (activeKeybindEvent) {
            case "MENU_LEFT":
            case "MOVE_LEFT":
                if (selectionId <= 0)
                    selectionId = playerSkinResources.size() - 1;
                else
                    selectionId--;
                playerState.setActivePlayerSkin(playerSkinResources.get(selectionId).skinDescriptor);
                break;
            case "MENU_RIGHT":
            case "MOVE_RIGHT":
                selectionId = ++selectionId % playerSkinResources.size();
                playerState.setActivePlayerSkin(playerSkinResources.get(selectionId).skinDescriptor);
                break;
            case "CONFIRM":
                PlayerSkinResource playerSkinResource = playerSkinResources.get(selectionId);
                if (PlayerSkinResourcePurchasable.class.isInstance(playerSkinResource)) {
                    PlayerSkinResourcePurchasable playerSkinResourcePurchasable = (PlayerSkinResourcePurchasable)playerSkinResource;
                    playerSkinsMarketplace.redeemOffer(playerState.getWalletBag(),
                                                       playerSkinResourcePurchasable.offerRequest);
                    sceneSwitchable.switchScene(CustomisationScene.class.getSimpleName());

                } else {
                    sceneSwitchable.switchScene("GameScene");
                }
                break;
            case "BACK":
                sceneSwitchable.switchScene("StartScene");
                break;
            }
            inputDebounce = System.currentTimeMillis();
            if (inputDebounceFirst) {
                inputDebounceFirst = false;
            } else {
                inputDebounceDelay = 100;
            }
        }
    }
}
