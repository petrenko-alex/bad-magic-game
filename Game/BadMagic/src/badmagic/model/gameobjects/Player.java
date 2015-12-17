package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;


public class Player extends GameObject {

    public Player(GameField field) {
        super(field);
        loadPic();
    }

    @Override
    public void paint(Graphics g, Point pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    protected void loadPic() {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _image = toolkit.getImage(PIC);
    }
    
    private static final String PIC = "/badmagic/resources/goat.png";
}
