
package bombermanserver.messages;

/**
 *
 * @author Eugenio
 */
public class BombMessage extends Message{
    private int x;
    private int y;

    /**
     * Constructor
     * @param x int x
     * @param y int y
     */
    public BombMessage(int x, int y){
        super(MessageType.BOMB_UPDATE);
        this.x = x;
        this.y = y;
    }
    
    // GETTER
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
