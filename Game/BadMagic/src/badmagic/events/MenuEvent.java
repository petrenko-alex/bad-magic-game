package badmagic.events;

import java.util.EventObject;


public class MenuEvent extends EventObject {

    public MenuEvent(Object o) {

        super(o);
    }

    public int getChoosenLevel() {

        return _choosenLevel;
    }

    public void setChoosenLevel(int choosenLevel) {

        _choosenLevel = choosenLevel;
    }

    private int _choosenLevel;
}
