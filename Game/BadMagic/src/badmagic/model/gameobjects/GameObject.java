package badmagic.model.gameobjects;

import badmagic.model.GameField;
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
    
    protected Point _position;
    protected GameField _field;
}
