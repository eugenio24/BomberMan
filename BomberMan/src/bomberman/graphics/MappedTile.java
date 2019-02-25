
package bomberman.graphics;

/**
 *
 * @author Eugenio
 */
public class MappedTile {
    private int tileID;
    private int x;
    private int y;
    
    private final int zoom = 4;

    /**
     * Constructor
     * @param tile Tile ID
     * @param x int x pos
     * @param y int y pos
     */
    public MappedTile(int tile, int x, int y){
        this.tileID = tile;
        this.x = x;
        this.y = y;
    }     

    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle(x, y, 16*zoom, 16*zoom);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MappedTile other = (MappedTile) obj;
        if (this.tileID != other.tileID) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    
    // GETTER

    public int getTileID() {
        return tileID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
