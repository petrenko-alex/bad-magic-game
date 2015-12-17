package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

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
    
    protected Point _position;
    protected GameField _field;
    protected Image _image;
}
