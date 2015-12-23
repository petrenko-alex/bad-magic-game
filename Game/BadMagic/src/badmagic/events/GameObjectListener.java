package badmagic.events;

import java.util.EventListener;


public interface GameObjectListener extends EventListener {

    void objectChangedPosition(GameObjectEvent event);
}
