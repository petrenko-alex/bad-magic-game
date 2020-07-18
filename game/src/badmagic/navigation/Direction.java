package badmagic.navigation;

/**
 * Класс представляющий направление.
 *
 * Направление задается как угол в градусах в системе координат.
 * Позволяет сравнивать направления, получать смысловые направления.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class Direction {

    /** Направление как градусы в системе координат */
    private int _angle = 90;

    /**
     * Конструктор класса с параметром.
     *
     * Создает направление с указанными градусами.
     * Приводит его к корректному при неправильном задании.
     *
     * @param angle градусы в системе координат
     */
    private Direction(int angle) {

        /* Приводим заданный угол к допустимому диапазону */
        angle = angle % 360;

        if(angle < 0) {

            angle += 360;
        }
        _angle = angle;
    }

    /**
     * Метод получения градусов текущего направления.
     *
     * @return int - количество градусов.
     */
    public int getAngle() {

        return _angle;
    }

    //////////////////////// Получение направлений ////////////////////////////

    /**
     * Метод получения направления - на север
     *
     * @return Direction - направление на север.
     */
    public static Direction north() {

        return new Direction(0);
    }

    /**
     * Метод получения направления - на юг.
     *
     * @return Direction - направление на юг.
     */
    public static Direction south() {

        return new Direction(180);
    }

    /**
     * Метод получения направления - на восток
     *
     * @return Direction - направление на восток.
     */
    public static Direction east() {

        return new Direction(90);
    }

    /**
     * Метод получения направления - на запад
     *
     * @return Direction - направление на запад.
     */
    public static Direction west() {

        return new Direction(270);
    }

    /**
     * Метод клонирования направления.
     *
     * @return Direction - точная копия направления.
     */
    @Override
    public Direction clone(){

        return new Direction(getAngle());
    }

    /**
     * Метод получения направления, повернутого по часовой стрелке на 90 градусов.
     *
     * @return Direction - направление, повернутое на 90 градусов по часовой стрелке.
     */
    public Direction clockwise() {

        return new Direction(getAngle() + 90);
    }

    /**
     * Метод получения направления, повернутого против часовой стрелки на 90 градусов.
     *
     * @return Direction - направление, повернутое на 90 градусов против часовой стрелки.
     */
    public Direction anticlockwise() {

        return new Direction(getAngle() - 90);
    }

    /**
     * Метод получения противоположного направления.
     *
     * @return Direction - противоположное направление.
     */
    public Direction opposite() {

        return new Direction(getAngle() + 180);
    }

    /**
     * Метод получения направления, повернутого направо.
     *
     * @return Direction - направление, повернутое направо.
     */
    public Direction rightword()  {

        return clockwise();
    }

    /**
     * Метод получения направления, повернутого налево.
     *
     * @return Direction - направление, повернутое налево.
     */
    public Direction leftword()  {

        return anticlockwise();
    }

    //////////////////////// Сравнение направлений ////////////////////////////

    /**
     * Метод сравнения двух направленией на идентичность.
     *
     * @param other направление, с которым сравнивают.
     * @return boolean - флаг - равны ли направления.
     */
    @Override
    public boolean equals(Object other) {

        if(other == null) {

            return false;

        } else if(other instanceof Direction) {

            Direction otherDirect = (Direction)other;
            return  getAngle() == otherDirect.getAngle();

        }
        return false;
    }

    /**
     * Хеш-функция.
     *
     * @return int - хеш-значение.
     */
    @Override
    public int hashCode() {

        return getAngle();
    }

    /**
     * Метод сравнения двух направлений на противоположность.
     *
     * @param other направление, с которым сравнивают.
     * @return boolean - флаг - противоположны ли направления.
     */
    public boolean isOpposite(Direction other) {

        return opposite().equals(other);
    }
}