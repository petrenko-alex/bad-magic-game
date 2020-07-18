package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Интерфейс слушателя событий модели.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public interface ModelListener extends EventListener {

    /**
     * Событие символизирует о том, что уровень пройден.
     *
     * @param e событие.
     */
    public void levelCompleted(EventObject e);

    /**
     * Событие символизирует о том, что уровень провален.
     *
     * @param e событие.
     */
    public void levelFailed(EventObject e);
}

