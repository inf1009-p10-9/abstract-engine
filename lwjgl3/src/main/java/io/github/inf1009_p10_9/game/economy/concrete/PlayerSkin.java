package io.github.inf1009_p10_9.game.economy.concrete;
public class PlayerSkin {
    private String displayName;
    private String texturePath;

    public PlayerSkin(String displayName, String texturePath) {
        this.displayName = displayName;
        this.texturePath = texturePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTexturePath() {
        return texturePath;
    }

    /*
      Allow two different instances with identical contents to equal.
      This allows PlayerSkins to be treated as fungible.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlayerSkin))
            return false;
        PlayerSkin other = (PlayerSkin)o;
        return ((getDisplayName().equals(other.getDisplayName())) &&
                (getTexturePath().equals(other.getTexturePath())));
    }
}
