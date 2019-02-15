
package bombermanserver.messages;

import bombermanserver.Direction;

/**
 *
 * @author Eugenio
 */
public class PlayerMessage extends Message{
    private final int x;
    private final int y;
    private final Direction direction;
    
    /**
     * Constructor
     * @param x int x
     * @param y int y 
     * @param direction Direction 
     */
    public PlayerMessage(int x, int y, Direction direction) {
        super(MessageType.PLAYER_UPDATE);
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
    // GETTER    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
}
