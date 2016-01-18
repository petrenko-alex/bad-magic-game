package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс, представляющий собой телепортатор.
 * Является наследником InteractiveObject
 * Имеет собственное графическое представление
 * Хранит позицию для телепортации
 * 
 * @author Alexander Lyashenko
 */
public class Teleport extends InteractiveObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Teleport(GameField field) {
        super(field);
        loadPic();
        openId = -1;
    }

    /**
     * Метод, устанавливающий позицию для телепортации. 
     * Позиция должна быть в пределах игрового поля
     * 
     * @param tPosition позиция для телепортации
     */
    public void setTPosition(Point tPosition){
        _tPosition = new Point(tPosition);
    }
    
    /**
     * Метод, устанавливающий ключ для объекта
     * @param key  идентификатор ключа
     */
    public void setLock (int key){
        openId = key;
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

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Метод для открытия объекта (снятия с него замка)
     * @param key - ключ
     * @return Флаг - открылся ли объект
     */
     @Override
    public boolean unlock(CollectableObject key) {
        if (((Spell) key).getId() == openId) {
            openId = -1;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод активации объекта.
     * Для конкретного объекта - телепортация игрока.
     * @return Флаг, возвращающий успех действия
     */
    @Override
    public boolean activate() {
        if (openId == -1){
            
            for (GameObject o : _field.getObjects()){
                if (o instanceof Player){
                    ((Player)o).setPosition(_tPosition);
                    break;
                }
            }
            
            return true;
        }
        else{
            return false;
        }
    }
     ///////////////////////////// Данные //////////////////////////////////////
    /**
     * Путь к файлу с изображением
     */
    private static final String PIC = "/badmagic/resources/teleport.png";
    
    /**
     * Позиция телепортации
     */
    private Point _tPosition;
}
