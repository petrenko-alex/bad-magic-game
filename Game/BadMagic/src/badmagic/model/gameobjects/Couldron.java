package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.lang.IllegalArgumentException;

public class Couldron extends MovableObject {

    public Couldron(GameField field) {
        super(field);
        loadPic();
    }

    /**
     * Перегрузка метода перемещения объекта в направлении.
     *
     * Осуществляет перемещение объекта на соседнюю клетку в
     * указанном направлении, если эта клетка свободна;
     * проверяет окружение и расталкивает/притягивает
     * соседние объекты этого же типа в зависимости
     * от полюса.
     *
     * @param moveDirection направление перемещения.
     */
    @Override
    public void move(Direction moveDirection) {

        super.move(moveDirection);

        if( !_field.isActiveObjectSet() ) {

            _field.setActiveObject(this);
            sideEffect();
            _field.unsetActiveObject();
        }

    }

    /**
     * Метод отрисовки объекта.
     *
     * @param g   среда отрисовки.
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

    private void sideEffect() {

        /* Получить предметы в соседних клатках */
        ArrayList<GameObject> surrounding = _field.getSurrounding(this.getClass(),
                                                                  _position,
                                                                  SCOPE);

        /* Новые позиции для предметов */
        int objNumber = surrounding.size();
        for( int i = 0; i < objNumber; ++i ) {

            Couldron couldron = (Couldron) surrounding.get(i);

            Direction moveDirection = null;
            Direction currentDirection = getDirectionToObject(couldron);
            int currentPole = getPoleNumberByDirection(currentDirection);

            /* Получить следующий полюс */
            Pole nextPole = getOppositePole(currentPole);

            if( _pole[currentPole].equals(nextPole) ) {

                /* Если полюса одинаковые  */
                moveDirection = currentDirection;

            } else {

                /* Если полюса разные */
                moveDirection = currentDirection.opposite();
            }

            /* Двигаем котел в заданном направлении */
            couldron.move(moveDirection);
        }
    }

    private Pole getOppositePole(int currentPoleNumber) {

        int nextPoleNumber = currentPoleNumber;
        if ( currentPoleNumber == 3 ||
             currentPoleNumber == 2    ) {

            return _pole[currentPoleNumber - 2];

        } else if ( currentPoleNumber == 0 ||
                    currentPoleNumber == 1    ) {

            return _pole[currentPoleNumber + 2];

        } else {

            throw new IllegalArgumentException();
        }
    }

    private int getPoleNumberByDirection(Direction direction) {

        if( direction.equals(Direction.north()) ) {

            return 0;

        } else if ( direction.equals(Direction.east()) ) {

            return 1;

        } else if ( direction.equals(Direction.south()) ) {

            return 2;

        } else if ( direction.equals(Direction.west()) ){

            return 3;

        } else {

            throw new IllegalArgumentException();
        }
    }

    private Direction getDirectionToObject(Couldron nextObject) {

        int curX = _position.x;
        int curY = _position.y;
        int nextX = nextObject._position.x;
        int nextY = nextObject._position.y;

        if( curX > nextX  ) {

            return Direction.west();

        } else if ( curX < nextX ) {

            return Direction.east();

        } else if ( curY > nextY ) {

            return Direction.north();

        } else if ( curY < nextY ) {

            return Direction.south();

        } else {

            return null;
        }
    }

    ///////////////////////////// Данные //////////////////////////////////////

    /** Путь к файлу с изображением */
    private static final String PIC = "/badmagic/resources/Couldron.png";
    private static final int SCOPE = 2;
    private enum Pole {
        POSITIVE,
        NEGATIVE
    }
    private Pole _pole[] = new Pole[4];
    {
        _pole[0] = Pole.POSITIVE;
        _pole[1] = Pole.NEGATIVE;
        _pole[2] = Pole.POSITIVE;
        _pole[3] = Pole.NEGATIVE;
    }
}
