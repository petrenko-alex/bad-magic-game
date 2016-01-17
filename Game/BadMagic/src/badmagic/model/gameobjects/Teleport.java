package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Teleport extends InteractiveObject {

    public Teleport(GameField field) {
        super(field);
        loadPic();
        openId = -1;
    }

    @Override
    public void paint(Graphics g, Point pos) {
        g.drawImage(_image, pos.x, pos.y, null);
    }

    @Override
    protected void loadPic() {
        try {

            _image = ImageIO.read(getClass().getResource(PIC));

        } catch ( IOException ex ) {

            ex.printStackTrace();
        }
    }

    @Override
    public boolean activateObject(int key) {
        return true;
    }
    
     ///////////////////////////// Данные //////////////////////////////////////

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/teleport.png";
}
