package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;

/**
 * Абстрактный класс - передвигаемый игровой объект.
 *
 * Наследник класса GameObject.
 * Содержит метод перемещения объекта.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class MovableObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public MovableObject(GameField field) {

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
