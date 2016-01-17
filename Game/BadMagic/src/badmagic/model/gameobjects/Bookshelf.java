package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Bookshelf extends InteractiveObject {

    public Bookshelf(GameField field) {
        super(field);
        openId = -1;
        loadPic();
    }

    public void setSpell(int spellId){
        _spellId = spellId;
    }
    
    public void setLock (int key){
        openId = key;
    }
    
    @Override
    public boolean unlock(CollectableObject key){
       if (((Spell)key).getId() == openId){
           openId = -1;
           return true;
       }
       else {
           return false;
       }
    }
    
    @Override
    public boolean activate() {
         if (openId == -1){
            /*Удаление полки с поля*/
           Point temp = new Point(_position);
           _field.removeObject(this);
           
            /*Добавление заклинания на его место*/
            if (_spellId != -1){
                 GameObject newSpell = new Spell(_field);
                ((Spell)newSpell).setId(_spellId);
                _field.addObject(temp, (Spell)newSpell);
            }
            return true;
        }
        else{
            return false;
        }
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
    private static final String PIC = "/badmagic/resources/bookshelf.png";
    
    /** Хранимое заклинание (-1 если отсутствует)*/
    private int _spellId = -1;

    
    
}
