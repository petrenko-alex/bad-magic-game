package badmagic.model.gameobjects;

import badmagic.events.GameObjectListener;
import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.EventObject;

public abstract class GameObject {

    public GameObject(GameField field) {

        _field = field;
    }

    public Point getPosition() {

        return _position;
    }

    public void unsetPosition() {

        _position = null;
    }

    public boolean setPosition(Point pos) {

        if( pos != null && _field.getObjects(pos).isEmpty() ) {

            _position = pos;
            return true;
        }
        return false;
    }

    public abstract void paint(Graphics g,Point pos);

    protected abstract void loadPic();

    ///////////////////////////////////////////////////////////////////////////
    public void addObjectListener(GameObjectListener l) {

        _listenerList.add(l);
    }

    public void removeObjectListener(GameObjectListener l) {

        _listenerList.remove(l);
    }

    protected void fireObjectChanged() {

        EventObject event = new EventObject(this);
        for (Object listener : _listenerList) {

            ((GameObjectListener) listener).objectChanged(event);
        }
    }

    private ArrayList _listenerList = new ArrayList();

    ///////////////////////////////////////////////////////////////////////////

    protected Point _position;
    protected GameField _field;
    protected Image _image;
}
