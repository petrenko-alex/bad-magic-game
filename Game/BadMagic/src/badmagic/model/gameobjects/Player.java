package badmagic.model.gameobjects;

import badmagic.BadMagic;
import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Player extends GameObject {

    public Player(GameField field) {
        super(field);
        loadPic();
    }

    public int getMoves() {

        return _moves;
    }

    public void setMoves(int moves) {

        _moves = moves;
    }

    public void move(Direction moveDirection) {

        move(moveDirection,true);
    }

    public void moveObject(Direction moveDirection) {

        GameObject nextObject = null;
        ArrayList<GameObject> objects = null;

        Point nextPos = _field.getNextPos(_position, _gazeDirection);

        /* Получаем объекты в этой позиции и ищем тот, который можно сдвинуть */
        objects = _field.getObjects(nextPos);
        if( !objects.isEmpty() ) {

            for(GameObject object : objects) {

                if( object instanceof MovableObject ) {

                    nextObject = object;
                }
            }
        }

        /* Пробуем сдвинуть */
        if( nextObject != null ) {

            if( _gazeDirection.equals(moveDirection) ) {

                /* Толкаем предмет вперед */
                ((MovableObject)nextObject).move(moveDirection);
                this.move(moveDirection,false);

                BadMagic.log.info("Толкаем предмет вперед.");

            } else if( _gazeDirection.isOpposite(moveDirection) ) {

                /* Тянем предмет на себя */
                this.move(moveDirection,false);
                ((MovableObject)nextObject).move(moveDirection);

                BadMagic.log.info("Тянем предмет на себя.");

            } else {

                _gazeDirection = moveDirection;

                BadMagic.log.info("Нельзя сдвинуть в этом направлении.");
            }
        }  else {

            _gazeDirection = moveDirection;

            BadMagic.log.info("Нечего двигать.");
        }
    }

    @Override
    public void paint(Graphics g,Point pos) {

        AffineTransform at = rotate(_gazeDirection,pos);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(_image, at, null);
    }

    @Override
    protected void loadPic() {

        try {

            _image = ImageIO.read(getClass().getResource(PIC));

        } catch ( IOException ex ) {

            ex.printStackTrace();
        }
    }

    private AffineTransform rotate(Direction direction,Point pos) {

        int angle = direction.getAngle();

        AffineTransform at = AffineTransform.getTranslateInstance(pos.x, pos.y);
        at.rotate(Math.toRadians(angle),
                  _image.getWidth() / 2,
                  _image.getHeight() / 2);
        return at;
    }

    private void move(Direction moveDirection,boolean needToChangeDirection) {

        if( needToChangeDirection ) {

            _gazeDirection = moveDirection;

            BadMagic.log.info("Направление взгляда изменено.");
        }

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
            _moves--;
            fireObjectMoved();

            BadMagic.log.info("Переход на клетку (" +
                              _position.x + ";" + _position.y + ").");
        } else {

            BadMagic.log.info("Невозможно перейти на клетку.");
        }
    }

    private int _moves;
    private Direction _gazeDirection = Direction.north();
    private static final String PIC = "/badmagic/resources/goat.png";
}
