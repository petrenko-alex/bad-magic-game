package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

public interface GameObjectListener extends EventListener {

    void objectChanged(EventObject e);
}
