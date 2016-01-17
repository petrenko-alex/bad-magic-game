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

    public void setTPosition(Point tPosition){
        _tPosition = new Point(tPosition);
    }
    
    public void setKey (int key){
        openId = key;
    }
    
    @Override
    public void paint(Graphics g, Point pos) {
        g.drawImage(_image, pos.x, pos.y, null);
    }

    @Override
    protected void loadPic() {
        try {

            _image = ImageIO.read(getClass().getResource(PIC));

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

     @Override
    public boolean unlock(CollectableObject key) {
        if (((Spell) key).getId() == openId) {
            openId = -1;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean activate() {
        if (openId == -1){
            
            for (GameObject o : _field.getObjects()){
                if (o instanceof Player){
                    ((Player)o).setPosition(_tPosition);
                    break;
                }
            }
            
            return true;
        }
        else{
            return false;
        }
    }
     ///////////////////////////// Данные //////////////////////////////////////
    /**
     * Путь к файлу с изображением
     */
    private static final String PIC = "/badmagic/resources/teleport.png";

   
    
    private Point _tPosition;
}
