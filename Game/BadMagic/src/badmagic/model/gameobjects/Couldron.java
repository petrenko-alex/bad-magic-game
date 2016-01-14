package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Color;
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
     * @return boolean - успешность перемещения.
     */
    @Override
    public boolean move(Direction moveDirection) {

        boolean isMoved = super.move(moveDirection);

        if( !_field.isActiveObjectSet() ) {

            _field.setActiveObject(this);
            sideEffect();
            _field.unsetActiveObject();
        }
        return isMoved;
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
        paintPole(g,pos);
    }

    private void paintPole(Graphics g, Point pos) {

        Color tmp = g.getColor();
        String str = "";

        for( int i = 0; i < _pole.length; ++i) {

            int x = pos.x;
            int y = pos.y;
            int offset = 15;
            int centerOffset = 5;

            if( _pole[i] == Pole.NEGATIVE) {

                g.setColor(Color.red);
                str = "-";

            } else {

                g.setColor(Color.green);
                str = "+";
            }

            if ( i == 0 ) {

                x += (_image.getWidth() / 2) - centerOffset;
                y += offset;

            } else if ( i == 1 ) {

                x += _image.getWidth() - offset;
                y += (_image.getHeight() / 2) + centerOffset;

            } else if ( i == 2 ) {

                x += (_image.getWidth() / 2) - centerOffset;
                y += _image.getHeight();

            } else if ( i == 3 ) {

                x += centerOffset;
                y += (_image.getHeight() / 2) + centerOffset;
            }

            g.drawString(str, x, y);
        }
        g.setColor(tmp);
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

    public void setPole(int[] pole) {

        for( int i = 0;i < POLE_NUMBER;++i ) {

            if( pole[i] == 0 ) {

                _pole[i] = Pole.NEGATIVE;

            } else if( pole[i] == 1 ) {

                _pole[i] = Pole.POSITIVE;
            }
        }
    }

    private Pole getPole(int poleNumber) {

        return _pole[poleNumber];
    }

    private void sideEffect() {

        /* Получить предметы в соседних клетках */
        ArrayList<GameObject> surrounding = _field.getSurrounding(this.getClass(),
                                                                  _position,
                                                                  SCOPE);

        /* Новые позиции для предметов */
        int objNumber = surrounding.size();
        for( int i = 0; i < objNumber; ++i ) {

            Direction moveDirection = null;

            /* Получаем соседний котел */
            Couldron couldron = (Couldron) surrounding.get(i);

            /* Полчаем направление, в котором находится соседний котел */
            Direction currentDirection = getDirectionToObject(couldron);

            /* Получаем полюс, направленный на соседний котел */
            int currentPoleNumber = getPoleNumberByDirection(currentDirection);
            Pole currentPole      = this.getPole(currentPoleNumber);

            /* Получаем полюс другого котла */
            int nextPoleNumber = getOppositePole(currentPoleNumber);
            Pole nextPole      = couldron.getPole(nextPoleNumber);

            if( currentPole.equals(nextPole) ) {

                /* Если полюса одинаковые  */
                moveDirection = currentDirection;
                BadMagic.log.info("Котлы разъезжаются.");

            } else {

                /* Если полюса разные */
                moveDirection = currentDirection.opposite();
                BadMagic.log.info("Котлы съезжаются.");
            }

            /* Двигаем котел в заданном направлении */
            couldron.move(moveDirection);
        }
    }

    private int getOppositePole(int currentPoleNumber) {


        if ( currentPoleNumber == 3 ||
             currentPoleNumber == 2    ) {

            return (currentPoleNumber - 2);

        } else if ( currentPoleNumber == 0 ||
                    currentPoleNumber == 1    ) {

            return (currentPoleNumber + 2);

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

    /** Количество возможных полюсов */
    private final static int POLE_NUMBER = 4;

    /** Область действия магнитных свойств котла */
    private static final int SCOPE = 2;

    /** Полюс котла */
    private enum Pole {
        POSITIVE,
        NEGATIVE
    }

    /** Полюса котла */
    private Pole _pole[] = new Pole[4];

    /** Полюса по умолчанию */
    {
        _pole[0] = Pole.POSITIVE;
        _pole[1] = Pole.NEGATIVE;
        _pole[2] = Pole.POSITIVE;
        _pole[3] = Pole.NEGATIVE;
    }
}
