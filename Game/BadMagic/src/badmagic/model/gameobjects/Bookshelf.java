package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс реализующий книжную полку (сундук-хранилище).
 * Является наследником InteractiveObject.
 * Полка может содержать одно заклинание (в виде численного идентификатора).
 * После активации удаляется с поля, на своем месте оставляя объект-заклинание
 * Имеет собственное графическое представление
 * @author Alexander Lyashenko
 */
public class Bookshelf extends InteractiveObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Bookshelf(GameField field) {
        super(field);
        openId = -1;
        loadPic();
    }

    public void setSpell(int spellId){
        _spellId = spellId;
    }
    
    /**
     * Метод, устанавливающий ключ для объекта
     * @param key  идентификатор ключа
     */
    public void setLock (int key){
        openId = key;
    }
    
    /**
     * Метод для открытия объекта (снятия с него замка)
     * @param key - ключ
     * @return Флаг - открылся ли объект
     */
    @Override
    public boolean unlock(CollectableObject key){
       if (((Spell)key).getId() == openId){
           openId = -1;
           return true;
       }
       else {
           return false;
       }
    }
    
    /**
     * Метод, активирующий объект.
     * Полка убирает себя с поля, и оставляет на своем месте заклинание.
     * @return Флаг успеха действия
     */
    @Override
    public boolean activate() {
         if (openId == -1){
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
    
    /**
     * Метод отрисовки объекта.
     *
     * @param g среда отрисовки.
     * @param pos позиция отрисоки.
     */
    @Override
    public void paint(Graphics g, Point pos) {
        g.drawImage(_image, pos.x, pos.y, null);
    }

    /**
     * Метод загрузки изображения объекта.
     */
    @Override
    protected void loadPic() {
        
         try {

            _image = ImageIO.read(getClass().getResource(PIC));

        } catch ( IOException ex ) {

            ex.printStackTrace();
        }
    }
    
     ///////////////////////////// Данные //////////////////////////////////////

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/bookshelf.png";
    
    /** Хранимое заклинание (-1 если отсутствует)*/
    private int _spellId = -1;

    
    
}
