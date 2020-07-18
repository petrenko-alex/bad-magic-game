package badmagic.events;

import java.util.EventObject;

/**
 * Класс события игрового меню.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class MenuEvent extends EventObject {

    /**
     * Конструктор класса
     *
     * @param o объект.
     */
    public MenuEvent(Object o) {

        super(o);
    }

    /**
     * Метод получения номера уровня.
     *
     * @return int - номер уровня.
     */
    public int getChoosenLevel() {

        return _choosenLevel;
    }

    /**
     * Метод установки номера уровня.
     *
     * @param choosenLevel номер уровня.
     */
    public void setChoosenLevel(int choosenLevel) {

        _choosenLevel = choosenLevel;
    }

    /** Номер выбранного уровня */
    private int _choosenLevel;
}
