
package bombermanserver.messages;

/**
 *
 * @author Eugenio
 */
public class InitialPositionMessage extends Message{
    private final int x;
    private final int y;    
    
    /**
     * Constructor
     * @param x int x
     * @param y int y      
     */
    public InitialPositionMessage(int x, int y) {
        super(MessageType.INITIAL_POSITION);
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
