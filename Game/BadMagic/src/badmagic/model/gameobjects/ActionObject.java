
package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Игровой объект, выполняющий специальное предписанное действие, когда игрок активирует его.
 * 
 * Объект нельзя подобрать в инвентарь.
 * Объект нельзя сдвинуть.
 * Объект выполняет специальное действие, если игрок активирует его.
 * Объект может быть заперт на замок.
 * 
 * @author Alexander Lyashenko
 */
public abstract class ActionObject extends GameObject {

    public ActionObject(GameField field) {
        super(field);
    }
    
    /**
     * Абстрактный метод, закрывающий объект
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public abstract boolean lock(GameObject key);
    
    /**
     * Абстрактный метод, открывающий объект
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public abstract boolean unlock(GameObject key);
    
    /**
     * Абстрактный метод, активирующий объект
     * @return Флаг успеха действия
     */
    public abstract boolean activate();
    
    /**
     * Метод проверки закрытости объекта.
     * 
     * Возвращает флаг состояния замка (открыто\закрыто)
     * @return идентификатор ключа
     */
    public abstract boolean isLocked();
}
