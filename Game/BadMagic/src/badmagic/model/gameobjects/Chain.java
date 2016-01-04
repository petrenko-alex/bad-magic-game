package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Chain extends MovableObject {

    Chain(GameField field,Chest startChest) {

        super(field);
        
        /* Формируем сцепку */
        try {

            buildChain(startChest);

        } catch ( ClassNotFoundException ex ) {

            ex.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g, Point pos) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected void loadPic() {
        throw new UnsupportedOperationException("Not supported.");
    }

    private void buildChain(Chest startChest) throws ClassNotFoundException {

        Chest chest = null;
        Point nextPos = null;
        ArrayList<GameObject> tmp;
        Class chestClass = Class.forName(CHEST_CLASS);

        /* Добавляем текущий сундук к сцепке */
        _chain.add(startChest);
        BadMagic.log.info("Добавлен сундук на (" +
                          startChest.getPosition().x +
                          ";" +
                          startChest.getPosition().y +
                          ")");

        /* Проверка с 4 сторон */

        /* Проверка сверху */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.north());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chest) ) {

                BadMagic.log.info("Присоединяем сундук сверху");
                buildChain(chest);
            }
        }

        /* Проверка справа */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.east());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chest) ) {

                BadMagic.log.info("Присоединяем сундук справа");
                buildChain(chest);
            }
        }

        /* Проверка снизу */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.south());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chest) ) {

                BadMagic.log.info("Присоединяем сундук снизу");
                buildChain(chest);
            }
        }

        /* Проверка слева */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.west());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !isInChain(chest) ) {

                BadMagic.log.info("Присоединяем сундук слева");
                buildChain(chest);
            }
        }
    }

    private boolean isInChain(Chest chest) {

        for( Chest i : _chain ) {

            if( i.getPosition().equals(chest.getPosition()) ) {

                return true;
            }
        }
        return false;
    }

    ///////////////////////////// Данные //////////////////////////////////////

    /** Массив сундуков - сцепка */
    ArrayList<Chest> _chain = new ArrayList<>();

    /** Путь к классу Chest */
    private final static String CHEST_CLASS =
                                "badmagic.model.gameobjects.Chest";
}
