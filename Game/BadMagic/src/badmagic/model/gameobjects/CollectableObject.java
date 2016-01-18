package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Абстрактный класс - подбираемый игровой объект.
 * Наследники класса могут быть подобраны игроком. 
 * Их нельзя сдвинуть, но они могут быть перекрыты наследником MovableObject
 * С ними нельзя взаимодействовать как с InteractiveObject. 
 * Чтобы подобрать наследника, игрок ддолжен пройти через него.
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
