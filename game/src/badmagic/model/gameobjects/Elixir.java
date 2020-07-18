package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс представляет игровой объект - элексир.
 *
 * Наследник класса CollectableItemObject. Реализует абстрактные методы.
 Имеет образ - изображение.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class Elixir extends CollectableItemObject {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Загружает изображение объекта.
     *
     * @param field ссылка на игровое поле.
     */
    public Elixir(GameField field) {
        super(field);
        PIC = "/badmagic/resources/Elixir.png";
        loadPic();
    }
}
