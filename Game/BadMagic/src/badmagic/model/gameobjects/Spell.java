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
    public Spell(GameField field, int id) {
        super(field);
        _id = id;
        PIC = "/badmagic/resources/spell.png";
        loadPic(); 
    }

    public boolean equals(Spell obj){
       return obj._id == _id; 
    }
    /** 
     * Идентификатор открываемого объекта 
     */
    private int _id = -1;
    
}
