package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс, представляющий собой телепортатор. Является наследником ActionObject
 * Имеет собственное графическое представление Хранит позицию для телепортации
 *
 * @author Alexander Lyashenko
 */
public class Teleport extends ActionObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Teleport(GameField field) {
        super(field);
        PIC = "/badmagic/resources/teleport.png";
        _lock = new MagicLock(_field);
        loadPic();
    }
    
    /**
     * Метод, устанавливающий позицию для телепортации. Позиция должна быть в
     * пределах игрового поля
     *
     * @param tPosition позиция для телепортации
     */
    public void setTeleportingPosition(Point tPosition) {
        _tPosition = new Point(tPosition);
    }

    /**
     * Метод активации объекта. Для конкретного объекта - телепортация игрока.
     *
     * @return Флаг, возвращающий успех действия
     */
    @Override
    public boolean activate() {
        if (!this.isLocked()) {

            for (GameObject o : _field.getObjects()) {
                if (o instanceof Player && ((Player)o).isRequestedAction()) {
                    ((Player) o).setPosition(_tPosition);
                    break;
                }
            }
            
            return true;
        } else {
            return false;
        }
    }
    ///////////////////////////// Данные //////////////////////////////////////

    /**
     * Позиция телепортации
     */
    private Point _tPosition;
}
