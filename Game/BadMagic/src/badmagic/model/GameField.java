package badmagic.model;

import badmagic.model.gameobjects.GameObject;
import badmagic.navigation.Direction;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GameField {

    public GameField(int width,int height) {

        setSize(width,height);
    }

    /////////////////// Работа с клетками поля ////////////////////////////////
    boolean isCellEmpty(Point pos) {

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

            if(object.getPosition().equals(pos) ) {

                isEmpty = false;
            }
        }
        return isEmpty;
    }

    boolean isNextCellEmpty(Point currentPos, Direction direction) {

        /* Находим следующую клетку */
        Point nextCell = null;
        int x = currentPos.x;
        int y = currentPos.y;

        if( direction.equals(Direction.north())
            && (y - 1) >= 1 ) {

            nextCell = new Point(x,y - 1);

        } else if ( direction.equals(Direction.south())
                    && (y+1) <= _height ) {

            nextCell = new Point(x,y + 1);

        } else if ( direction.equals(Direction.west())
                    && (x-1) >= 1 ) {

            nextCell = new Point(x - 1,y);

        } else if(  direction.equals(Direction.east())
                    && (x+1) <= _width ) {

            nextCell = new Point(x + 1,y);
        }

        return isCellEmpty(nextCell);
    }

    /////////////////// Работа с размерами поля ///////////////////////////////
    public void setSize(int width,int height) {

        setWidth(width);
        setHeight(height);
    }

    public int getWidth() {
        return _width;
    }

    public void setWidth(int width) {
        _width = width;
    }

    public int getHeight() {
        return _height;
    }

    public void setHeight(int height) {
        _height = height;
    }

    /////////////////// Работа с объектами поля ///////////////////////////////
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

    public ArrayList<GameObject> getObjects() {

        ArrayList<GameObject> objList = new ArrayList<>();

        for( Map.Entry<Class, ArrayList<GameObject>> entry :
             _gameObjects.entrySet()) {

            objList.addAll(entry.getValue());
        }

        return objList;
    }

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

    public ArrayList<GameObject> getObjects(Class objType) {

        ArrayList<GameObject> objList = new ArrayList<>();

        if( _gameObjects.containsKey(objType) ) {

            objList.addAll( _gameObjects.get(objType) );
        }
        return objList;
    }

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

    public boolean isPositionUnique(Point pos) {

        ArrayList<GameObject> objects = getObjects();

        for(GameObject obj : objects) {

            if(pos.equals(obj.getPosition())) {

                return false;
            }
        }
        return true;
    }

    public void clear() {

        _gameObjects.clear();
    }

    //////////////////////////////// Данные ///////////////////////////////////
    private int _width;
    private int _height;
    private Map<Class, ArrayList<GameObject>> _gameObjects = new HashMap<>();
}
