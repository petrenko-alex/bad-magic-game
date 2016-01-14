package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс представляет вспомогательный игровой объект - сцепка.
 *
 * Объект класса создается только при перемещении объекта типа Сундук и
 * является активным(может перемещаться) контейнером объектов типа Сундук.
 *
 * @author Alexander Petrenko
 */
public class Chain {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Формирует сцепку.
     *
     * @param field ссылка на игровое поле.
     * @param startChest сундук, с которого начинает формироваться сцепка.
     */
    Chain(GameField field,Chest startChest) {

        _field = field;

        /* Формируем сцепку */
        try {

            buildChain(startChest);

        } catch ( ClassNotFoundException ex ) {

            ex.printStackTrace();
        }
    }

    /**
     * Метод проверки, может ли сцепка сдвинуться в заданном направлении.
     *
     * Сцепка может сдвинуться, если могут сдвинуться все ее объекты.<br>
     * Если какой-либо объект сцепки не может сдвинуться т.к. ему мешает
     * другой объект этой же сцепки, то будет проверена возможность перемещения
     * крайнего объекта сцепки в этом же направлении.
     *
     * @param moveDirection направление передвижения..
     * @return boolean - может ли сцепки передвинуться.
     */
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

    /**
     * Метод перемещения сцепки в заданном направлении.
     *
     * При перемещении сцепки, перемещаются все ее объекты.
     *
     * @param moveDirection направление перемещения.
     */
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

    /**
     * Рекурсивный метод формирования сцепки.
     *
     * @param startChest объект, с которого начинается формирование сцепки.
     * @throws ClassNotFoundException выбрасывается, если не верно задано имя класса.
     */
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

    /**
     * Метод проверки, имеется ли в сцепке объект с позицией pos.
     *
     * @param pos позиция объекта.
     * @return boolean - имеется ли объект в сцепке.
     */
    private boolean hasObjectWithPos(Point pos) {

        for( Chest i : _chain ) {

            if( i.getPosition().equals(pos) ) {

                return true;
            }
        }
        return false;
    }

    /**
     * Метод проверки, может ли передвинуться крайний объект сцепки в направлении.
     *
     * @param currentObjectPos позиция текущго объекта.
     * @param moveDirection направление передвижения.
     * @return boolean - может ли передвинуться крайний объект в направлении.
     */
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

                if( _field.isPosEmpty(nextPos) ) {

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

    /** Ссылка на поле */
    private GameField _field;

    /** Массив сундуков - сцепка */
    ArrayList<Chest> _chain = new ArrayList<>();

    /** Путь к классу Chest */
    private final static String CHEST_CLASS =
                                "badmagic.model.gameobjects.Chest";
}
