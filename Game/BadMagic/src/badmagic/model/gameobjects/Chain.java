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

    public boolean canMove(Direction moveDirection) {

        /*
         * Проходим по всем объектам сцепки и проверяем
         * их следующие позиции в заданном направлении
         */
        for( Chest chest : _chain ) {

            Point chestPos = chest.getPosition();

            if( !(_field.isNextPosEmpty(chestPos,moveDirection)) ) {

                /*
                 * Если на следующей позиции сундук, то
                 * пройдем до конца сцепки в заданном направлении и
                 * проверим, может ли передвинуться последний объект сцепки
                 */

                Point nextPos = _field.getNextPos(chestPos, moveDirection);

                if( hasObjectWithPos(nextPos) ) {

                    if ( !canLastObjectMove( chestPos, moveDirection) ) {

                        return false;
                    }

                } else {

                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void move(Direction moveDirection) {

        /*
         * Проходим по всем объектам сцепки и
         * перемещаем их в заданном направлении.
         */
        for( Chest chest : _chain ) {

            Point currentPos = chest.getPosition();
            chest._position = _field.getNextPos(currentPos, moveDirection);
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

            if( chest != null && !hasObjectWithPos(chest.getPosition()) ) {

                BadMagic.log.info("Присоединяем сундук сверху");
                buildChain(chest);
            }
        }

        /* Проверка справа */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.east());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !hasObjectWithPos(chest.getPosition()) ) {

                BadMagic.log.info("Присоединяем сундук справа");
                buildChain(chest);
            }
        }

        /* Проверка снизу */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.south());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !hasObjectWithPos(chest.getPosition()) ) {

                BadMagic.log.info("Присоединяем сундук снизу");
                buildChain(chest);
            }
        }

        /* Проверка слева */
        nextPos = _field.getNextPos(startChest.getPosition(), Direction.west());
        tmp = _field.getObjects(chestClass,nextPos);
        if( !tmp.isEmpty() ) {

            chest = (Chest)tmp.get(0);

            if( chest != null && !hasObjectWithPos(chest.getPosition()) ) {

                BadMagic.log.info("Присоединяем сундук слева");
                buildChain(chest);
            }
        }
    }

    private boolean hasObjectWithPos(Point pos) {

        for( Chest i : _chain ) {

            if( i.getPosition().equals(pos) ) {

                return true;
            }
        }
        return false;
    }

    private boolean canLastObjectMove(Point currentObjectPos,
                                      Direction moveDirection) {

        boolean stop = false;
        boolean canMove = false;
        Point currentPos = (Point) currentObjectPos.clone();

        while( !stop ) {

            /* Получаем следующую позицию на поле в направлении */
            Point nextPos = _field.getNextPos(currentPos, moveDirection);

            if( nextPos == null ) {

                stop = true;
                continue;
            }

            /* Если объекта с такой позицией нет в сцепке */
            if( !hasObjectWithPos(nextPos) ) {

                /* Проверяем, свободна ли позиция на поле */
                if( _field.isPosEmpty(nextPos,false) ) {

                    canMove = true;

                } else {

                    canMove = false;
                }

                stop = true;
            }
            /* Переходим к следующей позиции */
            currentPos = nextPos;
        }

        return canMove;
    }

    ///////////////////////////// Данные //////////////////////////////////////

    /** Массив сундуков - сцепка */
    ArrayList<Chest> _chain = new ArrayList<>();

    /** Путь к классу Chest */
    private final static String CHEST_CLASS =
                                "badmagic.model.gameobjects.Chest";
}
