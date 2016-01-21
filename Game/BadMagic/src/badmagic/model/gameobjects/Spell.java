package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс, реализующий заклинание - ключ.
 * Является наследником CollectableItemObject.
 Имеет собственный численный идентификатор.
 Имеет собственное графическое представление.
 * 
 * @author Alexander Lyashenko
 */
public class Spell  extends CollectableItemObject  {
    
    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Spell(GameField field) {
        super(field);
        PIC = "/badmagic/resources/spell.png";
        loadPic();        
    }

    /**
     * Метод, устанавливающий идентификатор для заклинания.
     * 
     * @param _id идентификатор для заклинания
     * @return ссылка на объект
     */
    public Spell setId (int _id){
        id = _id;
        return this;
    }
    
    /**
     * Метод, возвращающий текущий идентификатор заклинания.
     * @return текущий идентификатор заклинания
     */
    public int getId (){
        return id;
    }
    
    /** 
     * Идентификатор открываемого объекта 
     */
    private int id = -1;
    
}
