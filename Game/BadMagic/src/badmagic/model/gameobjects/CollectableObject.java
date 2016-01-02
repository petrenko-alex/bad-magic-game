package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Абстрактный класс - подбираемый игровой объект.
 *
 * Наследник класса GameObject.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class CollectableObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public CollectableObject(GameField field) {

        super(field);
    }
}
