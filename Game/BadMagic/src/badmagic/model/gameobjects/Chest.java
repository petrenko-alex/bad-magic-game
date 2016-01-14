package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Chest extends MovableObject {

    public Chest(GameField field) {
        super(field);
        loadPic();
    }

    /**
     * Перегрузка метода перемещения объекта в направлении.
     *
     * ..................................................
     *
     * @param moveDirection направление перемещения.
     * @return boolean - успешность перемещения.
     */
    @Override
    public boolean move(Direction moveDirection) {

        /* Рекурсивно сформировать сцепку */
        Point startPos = (Point) _position.clone();

        BadMagic.log.info("Начато формирование сцепки...");

        Chain chain = new Chain(_field,this);

        BadMagic.log.info("Формирование сцепки завершено.");

        /* Проверить возможность перемещения сцепки */
        if( chain.canMove(moveDirection) ) {

            BadMagic.log.info("СЦЕПКУ МОЖНО ПЕРЕМЕСТИТЬ.");

            /* Перместить сцепку */
            chain.move(moveDirection);
            return true;

        } else {

            BadMagic.log.info("СЦЕПКУ НЕЛЬЗЯ ПЕРЕМЕСТИТЬ.");
            return false;
        }
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
