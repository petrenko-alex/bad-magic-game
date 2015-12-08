package badmagic.model;

import java.awt.Point;


public abstract class GameObject {
    
    public GameObject() {
        
        
    }
    
    public void setPosition(Point pos) {
        
        _position = pos;
    }
    
    public Point getPosition() {
        
        return _position;
    }
    
    public void unsetPosition() {
        
        _position = null;
    }
    
    private Point _position;
}
