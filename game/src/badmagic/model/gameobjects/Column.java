
package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;


public class Column extends UnmovableObject {

    public Column(GameField field) {
        super(field);
    }

    @Override
    public void paint(Graphics g, Point pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void loadPic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}