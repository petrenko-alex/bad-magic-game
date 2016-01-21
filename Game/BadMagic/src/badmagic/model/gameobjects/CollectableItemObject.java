package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Собираемый в инвентарь игрока игровой предмет
 * 
 * Объект может быть подобран игроком и убран в инвентарь. Чтобы подобрать объект, игрок ддолжен пройти через него.
 * Объект не может быть сдвинут.
 * Объект нельзя активировать.
 * 
 * Наследник класса GameObject.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class CollectableItemObject extends GameObject {

    /**
     * Конструктор класса.
     *
     * Вызывает конструктор базового класса.
     *
     * @param field ссылка на игровое поле.
     */
    public CollectableItemObject(GameField field) {

        super(field);
    }
}
