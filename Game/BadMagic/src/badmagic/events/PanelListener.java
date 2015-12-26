package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

public interface PanelListener  extends EventListener {

    public void mainMenuClicked(EventObject e);
}
