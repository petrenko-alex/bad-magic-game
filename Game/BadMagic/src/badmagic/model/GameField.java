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
     * Метод получения следующей позиции в указанном направлнеии.
     *
     * @param currentPos текущая позиция.
     * @param direction направление.
     * @return Point - следующая позиция.
     */
    public Point getNextPos(Point currentPos, Direction direction) {

        /* Проверка на невалидные аргументы */
        if( currentPos == null || direction == null ) {

            return null;
        }

        /* Находим следующую позицию */
        Point nextPos = null;
        int x = currentPos.x;
        int y = currentPos.y;

        if( direction.equals(Direction.north())
            && (y - 1) >= 1 ) {

            nextPos = new Point(x,y - 1);

        } else if ( direction.equals(Direction.south())
                    && (y+1) <= _height ) {

            nextPos = new Point(x,y + 1);

        } else if ( direction.equals(Direction.west())
                    && (x-1) >= 1 ) {

            nextPos = new Point(x - 1,y);

        } else if(  direction.equals(Direction.east())
                    && (x+1) <= _width ) {

            nextPos = new Point(x + 1,y);
        }

        return nextPos;
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
    
    public boolean isPosHasCollectable(Point pos) {

        /* Проверка переданных координат на принадлежность полю */
        if(   pos == null                   ||
            ((pos.x < 1 || pos.x > _width ) ||
             (pos.y < 1 || pos.y > _height))    ) {

            return false;
        }

        /* Поиск объектов с такой же позицией */
        boolean isEmpty = false;
        ArrayList<GameObject> fieldObjects = getObjects(pos);

        for(GameObject object : fieldObjects) {

            if(object instanceof CollectableObject) {

                isEmpty = true;
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
    
    public boolean isNextPosHasCollectable(Point currentPos, Direction direction){
        Point nextPos = getNextPos(currentPos,direction);
        return isPosHasCollectable(nextPos);
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

    //////////////////////////////// Данные ///////////////////////////////////

    /** Ширина поля */
    private int _width;

    /** Высота поля */
    private int _height;

    /** Игровые объекты поля */
    private Map<Class, ArrayList<GameObject>> _gameObjects = new LinkedHashMap<>();
}
