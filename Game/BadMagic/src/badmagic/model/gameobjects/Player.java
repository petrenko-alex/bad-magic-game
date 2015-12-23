package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;


public class Player extends GameObject {

    public Player(GameField field) {
        super(field);
        loadPic();
    }

    public void move(Direction moveDirection) {

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
        }
    }

    @Override
    public void paint(Graphics g, Point pos) {

        g.drawImage(_image, pos.x, pos.y, null);
    }

    @Override
    protected void loadPic() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _image = toolkit.getImage(PIC);
    }

    private Direction gazeDirection;
    private static final String PIC = "src/badmagic/resources/goat.png";
}
