package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Абстрактный класс - непередвигаемый игровой объект.
 *
 * Наследник класса GameObject.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class UnmovableObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public UnmovableObject(GameField field) {
        super(field);
    }

}
