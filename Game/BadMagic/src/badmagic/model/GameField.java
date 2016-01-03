package badmagic.model;

import badmagic.model.gameobjects.CollectableObject;
import badmagic.model.gameobjects.GameObject;
import badmagic.navigation.Direction;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс представляет игровую поле.
 *
 * Построен на основе полиморфного контейнера игровых объектов
 * с добавлением логики поля.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class GameField {

    /**
     * Конструктор игрового поля с параметрами.
     *
     * Устанавливает размер поля.
     *
     * @param width ширина поля.
     * @param height высота поля.
     */
    public GameField(int width,int height) {

        setSize(width,height);
    }

    /////////////////// Работа с клетками поля ////////////////////////////////

    /**
     * Метод получения nextIn позиции в указанном направлнеии относительно текущей.
     *
     * @param currentPos текущая позиция.
     * @param direction направление.
     * @param nextIn номер позиции в направлении относительно текущей
     * @return Point - следующая позиция.
     */
    public Point getPosIn(Point currentPos, Direction direction,int nextIn) {

        /* Проверка на невалидные аргументы */
        if( currentPos == null || direction == null ) {

            return null;
        }

        /* Находим следующую позицию */
        Point nextPos = null;
        int x = currentPos.x;
        int y = currentPos.y;

        if( direction.equals(Direction.north())
            && (y - nextIn) >= 1 ) {

            nextPos = new Point(x,y - nextIn);

        } else if ( direction.equals(Direction.south())
                    && (y+nextIn) <= _height ) {

            nextPos = new Point(x,y + nextIn);

        } else if ( direction.equals(Direction.west())
                    && (x-nextIn) >= 1 ) {

            nextPos = new Point(x - nextIn,y);

        } else if(  direction.equals(Direction.east())
                    && (x+nextIn) <= _width ) {

            nextPos = new Point(x + nextIn,y);
        }

        return nextPos;
    }

    /**
     * Метод получения следующей позиции в указанном направлнеии.
     *
     * @param currentPos текущая позиция.
     * @param direction направление.
     * @return Point - следующая позиция.
     */
    public Point getNextPos(Point currentPos, Direction direction) {

        return getPosIn(currentPos,direction,1);
    }

    /**
     * Метод проверки позиции на незанятость.
     *
     * @param pos позиция, которая проверяется.
     * @return boolean - флаг - занята ли позиция.
     */
    public boolean isPosEmpty(Point pos) {

        /* Проверка переданных координат на принадлежность полю */
        if(   pos == null                   ||
            ((pos.x < 1 || pos.x > _width ) ||
             (pos.y < 1 || pos.y > _height))    ) {

            return false;
        }

        /* Поиск объектов с такой же позицией */
        boolean isEmpty = true;
        ArrayList<GameObject> fieldObjects = getObjects();

        for(GameObject object : fieldObjects) {

            if(  object.getPosition().equals(pos) &&
               !(object instanceof CollectableObject)) {

                isEmpty = false;
            }
        }
        return isEmpty;
    }

    /**
     * Метод проверки следующей позиции в направлении на незанятость.
     *
     * @param currentPos текущая позиция.
     * @param direction направление.
     * @return boolean - флаг - занята ли следующая позиция.
     */
    public boolean isNextPosEmpty(Point currentPos, Direction direction) {

        Point nextPos = getNextPos(currentPos,direction);
        return isPosEmpty(nextPos);
    }

    /////////////////// Работа с размерами поля ///////////////////////////////

    /**
     * Метод установления размера поля
     *
     * @param width ширина
     * @param height высота
     */
    public void setSize(int width,int height) {

        setWidth(width);
        setHeight(height);
    }

    /**
     * Метод получения ширины поля
     *
     * @return int - ширина поля.
     */
    public int getWidth() {
        return _width;
    }

    /**
     * Метод установки ширины поля.
     *
     * @param width ширина.
     */
    public void setWidth(int width) {
        _width = width;
    }

    /**
     * Метод получения высоты поля
     *
     * @return int - высота поля.
     */
    public int getHeight() {
        return _height;
    }

    /**
     * Метод установки высоты поля.
     *
     * @param height высота.
     */
    public void setHeight(int height) {
        _height = height;
    }

    public int getDirectionsNumber() {

        return DIRECTIONS_NUMBER;
    }

    /////////////////// Работа с объектами поля ///////////////////////////////

    /**
     * Метод добавления объекта на поле.
     *
     * @param pos позиция объекта.
     * @param obj объект добавления.
     * @return boolean - флаг - успешност добавления.
     */
    public boolean addObject(Point pos, GameObject obj) {

       Class objClass = obj.getClass();

        if ( obj.setPosition(pos) ) {

            if( _gameObjects.containsKey(objClass) ) {

                _gameObjects.get(objClass).add(obj);

            } else {

                ArrayList<GameObject> objList = new ArrayList<>();
                objList.add(obj);
                _gameObjects.put(objClass, objList);
            }
            return true;
        }
        return false;
    }

    /**
     * Метод удаления объекта с поля.
     *
     * @param obj объект поля.
     * @return boolean - флаг - успешность удаления.
     */
    public boolean removeObject(GameObject obj) {

        boolean isSuccessful = false;
        Class objClass = obj.getClass();

        if( _gameObjects.containsKey(objClass) ) {

            isSuccessful = _gameObjects.get(objClass).remove(obj);

            if( isSuccessful ) {

                obj.unsetPosition();
            }
        }
        return isSuccessful;
    }

    /**
     * Метод получения объектов поля.
     *
     * @return ArrayList - все объекты поля.
     */
    public ArrayList<GameObject> getObjects() {

        ArrayList<GameObject> objList = new ArrayList<>();

        for( Map.Entry<Class, ArrayList<GameObject>> entry :
             _gameObjects.entrySet()) {

            objList.addAll(entry.getValue());
        }

        return objList;
    }

    /**
     * Метод получения объетов поля с заданной позицией.
     *
     * @param pos позиция объектов.
     * @return ArrayList - все объекты поля с заданной позицией.
     */
    public ArrayList<GameObject> getObjects(Point pos) {

        ArrayList<GameObject> objList = new ArrayList<>();

        for( Map.Entry<Class, ArrayList<GameObject> > entry :
             _gameObjects.entrySet()) {

            for( GameObject obj  : entry.getValue() ) {

                if( obj.getPosition().equals(pos) ) {

                    objList.add(obj);
                }
            }
        }
        return objList;
    }

    /**
     * Метод получения объектов поля заданного типа.
     *
     * @param objType тип объекта получения.
     * @return ArrayList - все объекты поля заданного типа.
     */
    public ArrayList<GameObject> getObjects(Class objType) {

        ArrayList<GameObject> objList = new ArrayList<>();

        if( _gameObjects.containsKey(objType) ) {

            objList.addAll( _gameObjects.get(objType) );
        }
        return objList;
    }

    /**
     * Метод получения объектов заданного типа и позиции.
     *
     * @param objType тип объекта получения.
     * @param pos позиция объекта получения.
     * @return ArrayList - все объекты поля заданного типа и позиции.
     */
    public ArrayList<GameObject> getObjects(Class objType, Point pos) {

        ArrayList<GameObject> objList = new ArrayList<>();

        if( _gameObjects.containsKey(objType) ) {

           for( GameObject obj  : _gameObjects.get(objType) ) {

                if( obj.getPosition().equals(pos) ) {

                    objList.add(obj);
                }
            }
        }
        return objList;
    }

    public ArrayList<GameObject> getSurrounding(Class objType,
                                                Point currentPos,
                                                int scope) {

        ArrayList<GameObject> objList = new ArrayList<>();
        Direction direction = Direction.north();

        for( int i = 0;i < DIRECTIONS_NUMBER;++i ) {

            GameObject obj = getNearestObject(objType,currentPos,direction,scope);
            if( obj != null ) {

                objList.add(obj);
            }
            direction = direction.clockwise();
        }

        return objList;
    }

    public GameObject getNearestObject( Class objType,
                                        Point currentPos,
                                        Direction direction,
                                        int scope) {

        for(int i = 1; i <= scope; ++i) {

            Point nextPos = getPosIn(currentPos, direction,i);
            ArrayList<GameObject> objects = getObjects(objType,nextPos);

            if( !objects.isEmpty() ) {

                return objects.get(0);
            }
        }

        return null;
    }

    /**
     * Метод проверки позиции на уникальность - незанятость.
     *
     * @param pos позиция, которая проверяется.
     * @return boolean - флаг - не занята ли позиция.
     */
    public boolean isPositionUnique(Point pos) {

        ArrayList<GameObject> objects = getObjects();

        for(GameObject obj : objects) {

            if(pos.equals(obj.getPosition())) {

                return false;
            }
        }
        return true;
    }

    /**
     * Метод очистки игрового поля.
     *
     * Очищает игровые объекты поля.
     */
    public void clear() {

        _gameObjects.clear();
    }

    public void setActiveObject(GameObject object) {

        _activeObject = object;
    }

    public void unsetActiveObject() {

        _activeObject = null;
    }

    public boolean isActiveObjectSet() {

        return _activeObject != null;
    }
    //////////////////////////////// Данные ///////////////////////////////////

    private GameObject _activeObject;

    /** Ширина поля */
    private int _width;

    /** Высота поля */
    private int _height;

    /** Игровые объекты поля */
    private Map<Class, ArrayList<GameObject>> _gameObjects = new LinkedHashMap<>();

    /** Количество возможных направлений поля */
    private final static int DIRECTIONS_NUMBER = 4;
}
