package badmagic.model.gameobjects;

import badmagic.events.GameObjectListener;
import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Абстрактный класс - игровой объект.<br>
 *
 * Хранит позицию игрового объекта и ссылку на класс игрового поля.<br>
 * Может посылать сообщения.<br>
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public abstract class GameObject {

    /**
     * Конструктор класса.<br>
     *
     * Инициализирует  поля класса.
     *
     * @param field ссылка на игровое поле
     */
    public GameObject(GameField field) {

        _field = field;
    }

    /**
     * Метод получения позиции объекта.<br>
     *
     * @return Point - позиция объекта.
     */
    public Point getPosition() {

        return _position;
    }

    /**
     * Метод обнуления позиции игрового объекта.
     */
    public void unsetPosition() {

        _position = null;
    }

    /**
     * Метод установки позиции игрового объекта.<br>
     * Позиция будет установлена, если передаваемый
     * параметр не выходит за границы поля и не null.
     *
     * @param pos позиция объекта.
     * @return boolean - успешность установки.
     */
    public boolean setPosition(Point pos) {

        if( pos != null ) {

            _position = pos;
            return true;
        }
        return false;
    }

    /**
     * Абстрактный метод отрисовки объекта.
     * @param g среда отрисовки.
     * @param pos позиция отрисовки
     */
    public abstract void paint(Graphics g,Point pos);

    /**
     * Абстрактный метод загрузки изображения объекта.
     */
    protected abstract void loadPic();

    ///////////////////// События игрового объекта ////////////////////////////

    /**
     * Метод добавления слушателя игрового объекта.
     * @param l слушатель.
     */
    public void addObjectListener(GameObjectListener l) {

        _listenerList.add(l);
    }

    /**
     * Метод удаления слушаетля игрового объекта.
     * @param l слушатель.
     */
    public void removeObjectListener(GameObjectListener l) {

        _listenerList.remove(l);
    }

    /**
     * Метод испускания сигнала о перемещении объекта.
     */
    protected void fireObjectMoved() {

        EventObject event = new EventObject(this);
        for (Object listener : _listenerList) {

            ((GameObjectListener) listener).objectMoved(event);
        }
    }

    /** Список слушателей игрового объекта */
    private ArrayList _listenerList = new ArrayList();

    ///////////////////////////// Данные //////////////////////////////////////

    /** Позиция игрового объекта */
    protected Point _position;

    /** Ссылка на игровое поле */
    protected GameField _field;

    /** Изображение объекта */
    protected BufferedImage _image;
}
