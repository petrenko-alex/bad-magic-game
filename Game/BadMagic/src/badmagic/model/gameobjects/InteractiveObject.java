
package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 * Абстрактный класс, реализующий интерактвный объект.
 * Наследник GameObject.
 * Наследники класса представляют собой интерактивный объект, который игрок может активировать.
 * Наследник может быть закрыт на замок. В таком случае для его активации,
 * игроку понадобится найти ключ и открыть наследника.
 * @author Alexander Lyashenko
 */
public abstract class InteractiveObject extends GameObject {

    public InteractiveObject(GameField field) {
        super(field);
    }
    
    /**
     * Абстрактный метод, открывающий объект
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public abstract boolean unlock(CollectableObject key);
    
    /**
     * Абстрактный метод, активирующий объект
     * @return Флаг успеха действия
     */
    public abstract boolean activate();
    
    /**
     * Метод проверки требования к ключу.
     *
     * Возвращает идентификатор заклинания, активирующего объект или -1 если заклинание не нужно.
     */
    public int needKey(){
        return openId;
    }
    
    /*Идентификатор заклинания, необходимый для активации*/
    int openId;
}
