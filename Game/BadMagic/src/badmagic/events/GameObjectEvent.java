package badmagic.events;

import badmagic.model.gameobjects.GameObject;
import badmagic.navigation.Direction;
import java.awt.Point;
import java.util.EventObject;


public class GameObjectEvent extends EventObject{

    public GameObjectEvent(Object source) {

        super(source);
    }

    /* --------------------------- Позиция ----------------------------------*/
    public Point getPos() {

        return _pos;
    }

    public void setPos(Point pos) {

        _pos = pos;
    }

    /* --------------------------- Объект -----------------------------------*/
    public GameObject getObject() {

        return _object;
    }

    public void setObject(GameObject object) {

        _object = object;
    }

    /* --------------------------- Направление ------------------------------*/
    public Direction getDirection() {

        return _direction;
    }

    public void setDirection(Direction direction) {

        _direction = direction;
    }

    private Point      _pos;
    private GameObject _object;
    private Direction  _direction;
}
