package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;


public class Bookshelf extends InteractiveObject {

    public Bookshelf(GameField field) {
        super(field);
    }

    @Override
    public void draw(Graphics g, Point pos) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
