package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Класс представляет игрока.
 *
 * Наследник класса GameObject. Реализует абстрактные методы.
 * Имеет образ - изображение, направление взгляда и количество ходов.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class Player extends GameObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Player(GameField field) {

        super(field);
        loadPic();
    }

    /**
     * Метод получения количества ходов игрока.
     *
     * @return int - количество ходов
     */
    public int getMoves() {

        return _moves;
    }

    /**
     * Метод установки количества ходов игрока.
     *
     * @param moves количество ходов.
     */
    public void setMoves(int moves) {

        _moves = moves;
    }

    /**
     * Метод перехода игрока на соседнюю клетку в направлении.
     *
     * Обертка над одноименным методом. Флаг изменения направления
     * взгляда установлен в true.
     *
     * @param moveDirection направление перемещения.
     * @return boolean - успешность перемещения.
     */
    public boolean move(Direction moveDirection) {

        return move(moveDirection,true);
    }

    /**
     * Метод перемещения объекта на соседнюю клетку.
     *
     * Перемещает игрока и объект на который он смотрит
     * на соседние клетки в направлении, если они свободны.
     *
     * @param moveDirection направление перемещения.
     */
    public void moveObject(Direction moveDirection) {

        GameObject nextObject = null;
        ArrayList<GameObject> objects = null;

        Point nextPos = _field.getNextPos(_position, _gazeDirection);

        /* Получаем объекты в этой позиции и ищем тот, который можно сдвинуть */
        objects = _field.getObjects(nextPos);
        if( !objects.isEmpty() ) {

            for(GameObject object : objects) {

                if( object instanceof MovableObject ) {

                    nextObject = object;
                }
            }
        }

        /* Пробуем сдвинуть */
        if( nextObject != null ) {

            if( _gazeDirection.equals(moveDirection) ) {

                /* Толкаем предмет вперед */
                ((MovableObject)nextObject).move(moveDirection);
                this.move(moveDirection,false);

            } else if( _gazeDirection.isOpposite(moveDirection) ) {

                /* Тянем предмет на себя */
                boolean isPlayerMoved = this.move(moveDirection,false);
                boolean isObjectMoved = ((MovableObject)nextObject).move(moveDirection);

                /* Если игрок передвинут, а объект нет */
                if( isPlayerMoved && !isObjectMoved ) {

                    this.move(moveDirection.opposite(),false);
                }

            } else {

                _gazeDirection = moveDirection;

            }
        }  else {

            _gazeDirection = moveDirection;

        }
    }

    /**
     * Метод отрисовки объекта.
     *
     * @param g среда отрисовки.
     * @param pos позиция отрисовки.
     */
    @Override
    public void paint(Graphics g,Point pos) {

        AffineTransform at = rotate(_gazeDirection,pos);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, at, null);
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

    /**
     * Метод поворота изображения игрока.
     *
     * @param direction направление поворота.
     * @param pos позиция объекта.
     * @return AffineTransform - объект трансформации.
     */
    private AffineTransform rotate(Direction direction,Point pos) {

        int angle = direction.getAngle();

        AffineTransform at = AffineTransform.getTranslateInstance(pos.x, pos.y);
        at.rotate(Math.toRadians(angle),
                  _image.getWidth() / 2,
                  _image.getHeight() / 2);
        return at;
    }

    /**
     * Метод перехода игрока на соседнюю клетку в направлении.
     *
     * Перемещает игрока на соседнюю клетку в заданном направлении,
     * если она свободна, изменяет направление взгляда, уменьшает
     * количество ходов, испускает сигнал о перемещении.
     *
     * @param moveDirection направление перемещения.
     * @param needToChangeDirection флаг - менять ли направление взгляда.
     * @return boolean - успешность перемещения.
     */
    private boolean move(Direction moveDirection,boolean needToChangeDirection) {

        if( needToChangeDirection ) {

            _gazeDirection = moveDirection;
        }

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
            _moves--;
            fireObjectMoved();

            return true;
            
        } else {

            return false;
        }
    }

    ///////////////////////////// Данные //////////////////////////////////////

    /** Количество ходов игрока */
    private int _moves;

    /** Направление взгляда игрока */
    private Direction _gazeDirection = Direction.north();

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/goat.png";
}
