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
     */
    @Override
    public void move(Direction moveDirection) {

        /* Рекурсивно сформировать сцепку */
        Point startPos = (Point) _position.clone();
        ArrayList<Chest> chain = new ArrayList();

        BadMagic.log.info("Начато формирование сцепки...");

        buildChain(chain,this);

        BadMagic.log.info("Формирование сцепки завершено.");

        /* Проверить возможность перемещения сцепки */
        /* Перместить сцепку */
        super.move(moveDirection);
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

    private void buildChain(ArrayList<Chest> chain,Chest startChest) {

        Point nextPos = null;
        ArrayList<GameObject> tmp;
        Chest chest = null;

        /* Добавляем текущий сундук к сцепке */
        chain.add(startChest);
        BadMagic.log.info("Добавлен сундук на (" +
                          startChest.getPosition().x +
                          ";" +
                          startChest.getPosition().y +
                          ")");

        /* Проверка с 4 сторон */

        /* Проверка сверху */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.north());
        tmp = _field.getObjects(this.getClass(),nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chain,chest) ) {

                BadMagic.log.info("Присоединяем сундук сверху");
                buildChain(chain,chest);
            }
        }

        /* Проверка справа */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.east());
        tmp = _field.getObjects(this.getClass(),nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chain,chest) ) {

                BadMagic.log.info("Присоединяем сундук справа");
                buildChain(chain,chest);
            }
        }

        /* Проверка снизу */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.south());
        tmp = _field.getObjects(this.getClass(),nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chain,chest) ) {

                BadMagic.log.info("Присоединяем сундук снизу");
                buildChain(chain,chest);
            }
        }

        /* Проверка слева */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.west());
        tmp = _field.getObjects(this.getClass(),nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chain,chest) ) {

                BadMagic.log.info("Присоединяем сундук слева");
                buildChain(chain,chest);
            }
        }
    }

    private boolean isInChain(ArrayList<Chest> chain, Chest chest) {

        for( Chest i : chain ) {

            if( i.getPosition().equals(chest.getPosition()) ) {

                return true;
            }
        }
        return false;
    }

    ///////////////////////////// Данные //////////////////////////////////////

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/Chest.png";
}
