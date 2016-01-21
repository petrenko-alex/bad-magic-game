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
        loadPic();
    }

    /**
     * Абстрактный метод, закрывающий объект
     * @param key - ключ
     * @return Флаг успеха действия
     */
    @Override
    public boolean lock(GameObject key) {
        if (key instanceof Spell) {
            _lockId = ((Spell) key).getId();
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

    /**
     * Идентификатор ключа-заклинания, которое открывает объект
     */
    private int _lockId = -1;

}
