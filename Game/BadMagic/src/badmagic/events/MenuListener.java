package badmagic.events;

import java.util.EventListener;

public interface MenuListener extends EventListener {

    public void startCareerClicked(MenuEvent e);
    public void continueCareerClicked(MenuEvent e);
    public void levelChoosen(MenuEvent e);
}
