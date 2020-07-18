package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Класс представляет игровой объект - сундук.
 *
 * Наследник класса MovableObject. Реализует абстрактные методы.
 * Имеет образ - изображение.<br>
 * При сближении с другими объектами этого же типа, происходит их объединение,
 * образуется сцепка сундуков, и в дальнейшем перемещаться будет не один сундук,
 * а вся сцепка сундуков.
 *
 * @author Alexander Petrenko
 */
public class Chest extends MovableObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Chest(GameField field) {

        super(field);
        loadPic();
    }

    /**
     * Метод перемещения объекта в направлении.
     *
     * Формирует сцепку объектов этого же типа(если они
     * находятся рядом), проверяет, может ли передвинуться все сцепка и,
     * если может, перемещает сцепку.<br>
     * Сцепка может передвинуться, если могут передвинуться все ее
     * составляющие объекты.<br>
     * При перемещении сцепки перемещаются все ее объекты в заданном направлении.
     *
     * @param moveDirection направление перемещения.
     * @return boolean - успешность перемещения.
     */
    @Override
    public boolean move(Direction moveDirection) {

        /* Рекурсивно сформировать сцепку */
        Point startPos = (Point) _position.clone();

        Chain chain = new Chain(_field,this);

        /* Проверить возможность перемещения сцепки */
        if( chain.canMove(moveDirection) ) {

            /* Перместить сцепку */
            chain.move(moveDirection);
            return true;

        } else {

            return false;
        }
    }

    /**
     * Метод отрисовки объекта.
     *
     * @param g среда отрисовки.
     * @param pos позиция отрисовки.
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
