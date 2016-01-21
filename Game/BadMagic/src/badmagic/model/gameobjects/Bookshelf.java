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
        loadPic();
    }

    public void setContainingItem(int spellId){
        _spellId = spellId;
    }
    
   /**
     * Абстрактный метод, закрывающий объект
     * @param key - ключ
     * @return Флаг успеха действия
     */
    @Override
    public boolean lock (GameObject key){
         if (key instanceof Spell){
             _lockId = ((Spell)key).getId();
             return true;
         }

         return false;
    }
    
    /**
     * Метод для открытия объекта (снятия с него замка)
     * @param key - ключ-заклинанеие
     * @return Флаг - открылся ли объект
     */
    @Override
    public boolean unlock(GameObject key){
        /** Проверяем, не пытаемся ли открыть открытое */
        if (!this.isLocked()){
            return true;
        }
        
        /** Если переданный ключ является заклинанием и идентификаторы совпали */
       if (key instanceof Spell && ((Spell)key).getId() == _lockId){
           _lockId = -1;
           return true;
       }
       else {
           return false;
       }
    }
    
    /**
     * Метод проверки закрытости объекта.
     * 
     * Возвращает флаг состояния замка (открыто\закрыто)
     * @return идентификатор ключа
     */
    @Override
    public boolean isLocked() {
        return (_lockId != -1);
    }
    
    /**
     * Метод, активирующий объект.
     * Полка убирает себя с поля, и оставляет на своем месте заклинание.
     * @return Флаг успеха действия
     */
    @Override
    public boolean activate() {
         if (_lockId == -1){
            /*Удаление полки с поля*/
           Point temp = new Point(_position);
           _field.removeObject(this);
           
            /*Добавление заклинания на его место*/
            if (_spellId != -1){
                 GameObject newSpell = new Spell(_field);
                ((Spell)newSpell).setId(_spellId);
                _field.addObject(temp, (Spell)newSpell);
            }
            return true;
        }
        else{
            return false;
        }
    }
    
     ///////////////////////////// Данные //////////////////////////////////////
    
    /** Хранимое заклинание (-1 если отсутствует)*/
    private int _spellId = -1;

    /** Идентификатор ключа-заклинания, которое открывает объект */
    private int _lockId = -1;
    
    
}
