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
 * Наследник класса GameObject. Реализует абстрактные методы. Имеет образ -
 * изображение, направление взгляда и количество ходов.
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
        _inventory = new ArrayList<GameObject>();
        PIC = "/badmagic/resources/goat.png";
        loadPic();
    }

    /**
     * Метод проверки наличия эликсира в инвентаре
     * @return Флаг наличия эликсира в инвентаре
     */
    public boolean haveElixirInInventory() {
        for (GameObject item : _inventory) {
            /*В инвентаре есть эликсир*/
            if (item instanceof Elixir) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Метод очистки инвентаря
     */
    public void clearInventory() {
        _inventory.clear();
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
     * Обертка над одноименным методом. Флаг изменения направления взгляда
     * установлен в true.
     *
     * @param moveDirection направление перемещения.
     */
    public void move(Direction moveDirection) {

        move(moveDirection, true);
    }

    /**
     * Метод перемещения объекта на соседнюю клетку.
     *
     * Перемещает игрока и объект на который он смотрит на соседние клетки в
     * направлении, если они свободны.
     *
     * @param moveDirection направление перемещения.
     */
    public void moveObject(Direction moveDirection) {

        GameObject nextObject = null;
        ArrayList<GameObject> objects = null;

        Point nextPos = _field.getNextPos(_position, _gazeDirection);

        /* Получаем объекты в этой позиции и ищем тот, который можно сдвинуть */
        objects = _field.getObjects(nextPos);
        if (!objects.isEmpty()) {

            for (GameObject object : objects) {

                if (object instanceof PushableObject) {

                    nextObject = object;
                }
            }
        }

        /* Пробуем сдвинуть */
        if (nextObject != null) {

            if (_gazeDirection.equals(moveDirection)) {

                /* Толкаем предмет вперед */
                ((PushableObject) nextObject).move(moveDirection);
                this.move(moveDirection, false);

                BadMagic.log.info("Толкаем предмет вперед.");

            } else if (_gazeDirection.isOpposite(moveDirection)) {

                /* Тянем предмет на себя */
                this.move(moveDirection, false);
                ((PushableObject) nextObject).move(moveDirection);

                BadMagic.log.info("Тянем предмет на себя.");

            } else {

                _gazeDirection = moveDirection;

                BadMagic.log.info("Нельзя сдвинуть в этом направлении.");
            }
        } else {

            _gazeDirection = moveDirection;

            BadMagic.log.info("Нечего двигать.");
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
        AffineTransform at = rotate(_gazeDirection, pos);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(_image, at, null);       
    }

    /**
     * Метод поворота изображения игрока.
     *
     * @param direction направление поворота.
     * @param pos позиция объекта.
     * @return AffineTransform - объект трансформации.
     */
    private AffineTransform rotate(Direction direction, Point pos) {

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
     * Перемещает игрока на соседнюю клетку в заданном направлении, если она
     * свободна, изменяет направление взгляда, уменьшает количество ходов,
     * испускает сигнал о перемещении.
     *
     * @param moveDirection направление перемещения.
     * @param needToChangeDirection флаг - менять ли направление взгляда.
     */
    private void move(Direction moveDirection, boolean needToChangeDirection) {

        if (needToChangeDirection) {

            _gazeDirection = moveDirection;

            BadMagic.log.info("Направление взгляда изменено.");
        }

        Point newPosition = _field.getNextPos(_position, moveDirection);

        if (_field.isPosEmpty(newPosition)) {

            if (_field.isPosHasCollectable(newPosition)) {
                /*Take item to inventory*/
                getItemFromField(newPosition);
            }

            _position = newPosition;
            _moves--;
            fireObjectMoved();

            BadMagic.log.info("Переход на клетку ("
                    + _position.x + ";" + _position.y + ").");
        } else {

            BadMagic.log.info("Невозможно перейти на клетку.");
        }
    }

    /**
     * Метод, убирающий предмет с поля в инвентарь
     * @param pos - позиция, с которой нужно собрать предметы
     */
    private void getItemFromField(Point pos) {

        ArrayList<GameObject> itemList = _field.getObjects(pos);

        for (GameObject obj : itemList) {

            if (obj instanceof CollectableItemObject) {
                _inventory.add(((CollectableItemObject)obj).removeFromField());
            }
        }

    }

    /**
     * Метод, активирующий объект в клетке по направлению
     */
    public void activateObject() {

        Point objectPosition = _field.getNextPos(_position, _gazeDirection);
        this.requestAction = true;
        
        for (GameObject obj : _field.getObjects(objectPosition)) {
            
            if (obj instanceof ActionObject) {
                /*Попытка открыть объект при необходимости*/
                 if (((ActionObject) obj).isLocked()){
                      for (GameObject o : _inventory) {
                        if (((ActionObject) obj).unlock(o)) {
                            _inventory.remove(o);
                            ((ActionObject) obj).activate();
                            break;
                        }
                    }
                 }
                 else{
                     ((ActionObject) obj).activate();
                 }
               
            }
            
        }
        
        this.requestAction = false;
    }
    
    public boolean isRequestedAction(){
        return requestAction;
    }

    ///////////////////////////// Данные //////////////////////////////////////
    /**
     * Количество ходов игрока
     */
    private int _moves;

    /**
     * Направление взгляда игрока
     */
    private Direction _gazeDirection = Direction.north();

    /**
     * Флаг запроса действия
     */
    private boolean requestAction;
    
    /**
     * Инвентарь
     */
    private ArrayList<GameObject> _inventory;
    
   
}
