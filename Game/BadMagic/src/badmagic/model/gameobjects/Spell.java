package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Spell  extends CollectableObject  {
    /** Идентификатор открываемого объекта */
    private int id = 0;
    
    public void setId (int _id){
        id = _id;
    }
    
    public int getId (int _id){
        return id;
    }
    
    public Spell(GameField field) {
        super(field);
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
    
     ///////////////////////////// Данные //////////////////////////////////////

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/spell.gif";
}
