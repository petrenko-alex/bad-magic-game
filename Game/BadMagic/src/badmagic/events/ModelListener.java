package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

public interface ModelListener extends EventListener {

    public void levelCompleted(EventObject e);
    public void levelFailed(EventObject e);
}

