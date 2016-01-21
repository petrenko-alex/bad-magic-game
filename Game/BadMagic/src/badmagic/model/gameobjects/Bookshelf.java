package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс реализующий книжную полку (сундук-хранилище).
 * Является наследником ActionObject.
 Полка может содержать одно заклинание (в виде численного идентификатора).
 После активации удаляется с поля, на своем месте оставляя объект-заклинание
 Имеет собственное графическое представление
 * @author Alexander Lyashenko
 */
public class Bookshelf extends ActionObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Bookshelf(GameField field) {
        super(field);
        PIC = "/badmagic/resources/bookshelf.png";
        _lock = new MagicLock(_field);
        loadPic();
    }

    public void setContainingItem(GameObject item){
        _containingItem = item;
    }
    
    /**
     * Метод, активирующий объект.
     * Полка убирает себя с поля, и оставляет на своем месте заклинание.
     * @return Флаг успеха действия
     */
    @Override
    public boolean activate() {
         if (!_lock.isLocked()){
            /*Удаление полки с поля*/
           Point temp = new Point(_position);
           _field.removeObject(this);
           
            /*Добавление заклинания на его место*/
            if (_containingItem != null){
                 _field.addObject(temp, _containingItem);
            }
            return true;
        }
        else{
            return false;
        }
    }
    
     ///////////////////////////// Данные //////////////////////////////////////
    
    /** Хранимое заклинание */
    private GameObject _containingItem;
}
