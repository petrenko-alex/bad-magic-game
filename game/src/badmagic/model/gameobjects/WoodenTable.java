package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс представляет игровой объект - деревянный стол.
 *
 * Наследник класса PushableObject. Реализует абстрактные методы.
 Имеет образ - изображение.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class WoodenTable extends PushableObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public WoodenTable(GameField field) {
        super(field);
        PIC = "/badmagic/resources/woodentable.png";
        loadPic();
    }
}
