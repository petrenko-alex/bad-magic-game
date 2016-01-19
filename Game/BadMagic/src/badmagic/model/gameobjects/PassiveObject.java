package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Класс-обобщение игровых объектов, не выполняющих никаких функций и не вступающих в отношения с игроком.
 * 
 * Объект нельзя подобрать в инвентарь.
 * Объект нельзя сдвинуть.
 * Объект нельзя активировать.
 * 
 * Наследник класса GameObject.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class PassiveObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public PassiveObject(GameField field) {
        super(field);
    }

}
