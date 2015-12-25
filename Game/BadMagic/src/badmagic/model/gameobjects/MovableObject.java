package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.navigation.Direction;


public abstract class MovableObject extends GameObject {

    public MovableObject(GameField field) {
        super(field);
    }

    public void move(Direction moveDirection) {

        if( _field.isNextPosEmpty(_position,moveDirection) ) {

            _position = _field.getNextPos(_position,moveDirection);
        }
    }

}
