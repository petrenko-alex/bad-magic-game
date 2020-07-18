
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
    public boolean isLocked() {
        return _lock.isLocked();
    }
    
     /**
     * Метод, закрывающий объект на новый ключ
     *
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public boolean lock(GameObject key){
        return _lock.lock(key);
    }

    /**
     * Метод, закрывающий объект на старый ключ. 
     * При отсутствии старого ключа, дейстивие игнорируется
     *
     * @return Флаг успеха действия
     */
    public boolean lock() {
       return _lock.lock();
    }

    /**
     * Метод, открывающий объект
     *
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public boolean unlock(GameObject key){
        return _lock.unlock(key);
    }
    
    protected Lock _lock;
}
