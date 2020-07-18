package badmagic.events;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Интерфейс слушателя событий игрового объекта.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public interface GameObjectListener extends EventListener {

    /**
     * Событие символизирует о том, что объект перемещен.
     *
     * @param e событие.
     */
    void objectMoved(EventObject e);
}
