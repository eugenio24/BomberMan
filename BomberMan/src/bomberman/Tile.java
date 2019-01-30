
package bomberman;

/**
 *
 * @author Eugenio
 */
public class Tile {
    private String tileName;
    private Sprite sprite;

    public Tile(String tileName, Sprite sprite) {
        this.tileName = tileName;
        this.sprite = sprite;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
    
}
