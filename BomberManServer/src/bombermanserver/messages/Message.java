
package bombermanserver.messages;

import java.io.Serializable;

/**
 *
 * @author Eugenio
 */
public class Message implements Serializable{
    private final MessageType messageType;
    
    /**
     * Constructor
     * @param type MessageType 
     */
    public Message(MessageType type){
        this.messageType = type;
    }
    
    // GETTER    
    public MessageType getMessageType(){
        return this.messageType;
    }
}
