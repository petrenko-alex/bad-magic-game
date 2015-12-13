package badmagic.events;

import java.awt.Event;
import java.util.EventListener;
import java.util.EventObject;


public interface MenuListener extends EventListener {
    
    public void startCareerClicked(EventObject e);
    public void continueCareerClicked(EventObject e);
}
