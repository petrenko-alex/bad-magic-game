package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Player extends GameObject {

    public Player(GameField field) {
        super(field);
        loadPic();
    }

    public void move(Direction moveDirection) {

        _gazeDirection = moveDirection;

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
        }

        fireObjectChanged();
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

    private Direction _gazeDirection = Direction.north();
    private static final String PIC = "/badmagic/resources/goat.png";
}
