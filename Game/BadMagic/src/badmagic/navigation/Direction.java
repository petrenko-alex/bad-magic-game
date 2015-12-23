package badmagic.navigation;

public class Direction {


    private int _angle = 90;

    private Direction(int angle) {

        /* Приводим заданный угол к допустимому диапазону */
        angle = angle % 360;

        if(angle < 0) {

            angle += 360;
        }
        _angle = angle;
    }

    /* ------------------ Возможные направления --------------------- */

    public static Direction north() {

        return new Direction(90);
    }

    public static Direction south() {

        return new Direction(270);
    }

    public static Direction east() {

        return new Direction(0);
    }

    public static Direction west() {

        return new Direction(180);
    }


    /* ------------------ Новые направления ------------------------- */

    @Override
    public Direction clone(){

        return new Direction(_angle);
    }

    public Direction clockwise() {

        return new Direction(_angle - 90);
    }

    public Direction anticlockwise() {

        return new Direction(_angle + 90);
    }

    public Direction opposite() {

        return new Direction(_angle + 180);
    }

    public Direction rightword()  {

        return clockwise();
    }

    public Direction leftword()  {

        return anticlockwise();
    }

    /* ------------------ Сравнить направления ---------------------- */

    @Override
    public boolean equals(Object other) {

        if(other == null) {

            return false;

        } else if(other instanceof Direction) {

            Direction otherDirect = (Direction)other;
            return  _angle == otherDirect._angle;

        }
        return false;
    }

    @Override
    public int hashCode() {

        return _angle;
    }

    public boolean isOpposite(Direction other) {

        return opposite().equals(other);
    }
}