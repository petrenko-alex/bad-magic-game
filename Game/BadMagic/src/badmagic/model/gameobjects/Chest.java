
package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Chest extends MovableObject {

    public Chest(GameField field) {
        super(field);
        loadPic();
    }

    /**
     * Метод отрисовки объекта.
     *
     * @param g среда отрисовки.
     * @param pos позиция отрисоки.
     */
    @Override
    public void paint(Graphics g, Point pos) {

        g.drawImage(_image, pos.x, pos.y, null);
    }

    /**
     * Метод загрузки изображения объекта.
     */
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
    private static final String PIC = "/badmagic/resources/Chest.png";
}
