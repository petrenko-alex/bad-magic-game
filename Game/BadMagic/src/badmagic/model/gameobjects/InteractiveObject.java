
package badmagic.model.gameobjects;

import badmagic.model.GameField;


public abstract class InteractiveObject extends GameObject {

    public InteractiveObject(GameField field) {
        super(field);
    }
    
    public abstract boolean unlock(int key);
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
