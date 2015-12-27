package badmagic.events;

import java.util.EventListener;

/**
 * Интерфейс слушателя событий меню.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public interface MenuListener extends EventListener {

    /**
     * Событие символизирует о том, что
     * нажата кнопка "Новая игра".
     *
     * @param e событие.
     */
    public void startCareerClicked(MenuEvent e);

    /**
     * Событие символизирует о том, что
     * нажата кнопка "Продолжить".
     *
     * @param e событие.
     */
    public void continueCareerClicked(MenuEvent e);

    /**
     * Событие символизирует о том, что
     * нажата кнопка "Выбор уровня".
     *
     * @param e событие.
     */
    public void levelChoosen(MenuEvent e);
}
