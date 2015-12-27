package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Интерфейс слушателя событий панели.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public interface PanelListener  extends EventListener {

    /**
     * Событие символизирует о том, что
     * нажата кнопка "Главное меню".
     *
     * @param e событие.
     */
    public void mainMenuClicked(EventObject e);
}
