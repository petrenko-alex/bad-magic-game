package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;

/**
 * Игровой объект, который игрок может двигать.
 *
 * Объект нельзя подобрать в инвентарь.
 * Объект можно двигать по игровому полю.
 * Объект нельзя активировать.
 * 
 * Наследник класса GameObject.
 * 
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class PushableObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public PushableObject(GameField field) {

        super(field);
    }

    /**
     * Метод перемещения объекта в направлении.
     *
     * Осуществляет перемещение объекта на соседнюю клетку в
     * указанном направлении, если эта клетка свободна.
     *
     * @param moveDirection направление перемещения.
     */
    public void move(Direction moveDirection) {

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
        }
    }
}
