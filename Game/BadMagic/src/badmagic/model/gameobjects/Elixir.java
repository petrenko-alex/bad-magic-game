package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;


public class Elixir extends CollectableObject {

    public Elixir(GameField field) {
        super(field);
        loadPic();
    }

    @Override
    public void paint(Graphics g, Point pos) {
        
        g.drawImage(_image, pos.x, pos.y, null);
    }
    
    @Override
    protected void loadPic() {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _image = toolkit.getImage(PIC);
    }
    
    private static final String PIC = "src/badmagic/resources/Elixir.png";
}
