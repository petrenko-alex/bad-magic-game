package badmagic.model;

import badmagic.BadMagic;
import badmagic.model.gameobjects.GameObject;
import badmagic.model.gameobjects.WoodenTable;
import java.awt.Point;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import badmagic.model.gameobjects.GameObjectsFactory;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Класс представляет игровой уровень.
 *
 * Считывает уровень из json файла
 * и хранит информацию о нем.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class Level {

    /**
     * Конструктор класса.
     *
     * Загружает уровнень игры.
     *
     * @param levelPath путь к файлу уровня.
     * @throws Exception при возникновении ошибок загрузки или парсинга файла.
     */
    Level(String levelPath) throws Exception {

        loadLevel(levelPath);
    }

    /**
     * Загрузить уровень из файла.
     *
     * Загружает json файл уровня, считывает его данные
     * и инициализирует поля класса.
     *
     * @param levelPath путь к файлу уровня.
     * @throws Exception при возникновении ошибок загрузки или парсинга.
     */
    public void loadLevel(String levelPath) throws Exception {

        _levelPath = levelPath;
        Point size;
        JSONParser parser = new JSONParser();

        InputStream inputStream = getClass().getResourceAsStream(levelPath);
        Object object = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        JSONObject json = (JSONObject) object;

        /* Название уровня */
        if ( json.containsKey("LevelName") ) {

            _name = (String) json.get("LevelName");
        }

        /* История уровня */
        if ( json.containsKey("LevelHistory") ) {

            _history = (String) json.get("LevelHistory");
        }

        /* Количество ходов игрока */
        if ( json.containsKey("PlayerMoves") ) {

            _moves = Integer.parseInt(json.get("PlayerMoves").toString());
        }

        /* Размер поля */
        parseFieldSize(json);

        /* Объекты поля */
        parseObjects(json);
    }

    /**
     * Метод получения количества ходов уровня.
     *
     * @return int - количество ходов уровня.
     */
    public int getMoves() {

        return _moves;
    }

    /**
     * Метод получения поля уровня.
     *
     * @return GameField - поле уровня.
     */
    public GameField getField() {

        return _field;
    }

    /**
     * Метод получения имени уровня.
     *
     * @return String - имя уровня.
     */
    public String getName() {

        return _name;
    }

    /**
     * Метод получения флага - пройден ли уровень.
     *
     * @return boolean - флаг - пройден ли уровень.
     */
    public boolean isCompleted() {

        return _isCompleted;
    }

    /**
     * Метод установки флага - пройден ли уровень.
     *
     * @param isCompleted пройден ли уровень.
     */
    public void setIsCompleted(boolean isCompleted) {

        _isCompleted = isCompleted;
    }

    /**
     * Метод получения истории уровня.
     *
     * @return String - история уровня.
     */
    public String getHistory() {

        return _history;
    }

    /**
     * Метод разбора и установки размеров полей.
     *
     * Находит в json объекте данные о размерах поля
     * и устанавливает их как размеры поля _field.
     *
     * @param json объект файла уровня.
     * @throws Exception при возникновении ошибок разбора.
     */
    private void parseFieldSize(JSONObject json) throws Exception {

        if ( json.containsKey("FieldSize") ) {

            JSONArray tmp = (JSONArray) json.get("FieldSize");
            int width = Integer.parseInt(tmp.get(0).toString());
            int height = Integer.parseInt(tmp.get(1).toString());

            if ( width > MAX_WIDTH || height > MAX_HEIGHT ) {

                Exception ex = new Exception("Размер поля ("
                                             + tmp.get(0).toString() + ";"
                                             + tmp.get(1).toString()
                                             + ") превышет "
                                             + "максимально допустимый ("
                                             + MAX_WIDTH + ";" + MAX_HEIGHT + ")");
                throw ex;

            } else if ( width < 1 || height < 1) {

                Exception ex = new Exception("Размер поля ("
                                             + tmp.get(0).toString() + ";"
                                             + tmp.get(1).toString()
                                             + ") не допустим");
                throw ex;
            }

            _field = new GameField(width, height);

        } else {

            Exception ex = new Exception("Не задан размер поля в файле уровня");
            throw ex;
        }
    }

    /**
     * Метод разбора и установки игровых объектов.
     *
     * Находит в json объекте данные об игровых объектах
     * и устанавливает их как объекты поля _field.
     *
     * @param json объект файла уровня.
     * @throws Exception при возникновении ошибок разбора.
     */
    private void parseObjects(JSONObject json) throws Exception {

        if ( json.containsKey("FieldObjects") ) {

            JSONArray objects = (JSONArray) json.get("FieldObjects");

            /* Парсинг объектов в массиве objects */
            for ( Object i : objects ) {

                JSONObject obj = (JSONObject) i;
                Set keys = obj.keySet();

                /*
                 * Ключ - конкретный тип объекта,
                 * должен соответствовать имени класса
                 */
                for ( Object key : keys ) {

                    JSONObject tmp = (JSONObject) obj.get(key.toString());
                    parseObject(tmp, key.toString());
                }
            }
        } else {

            Exception ex = new Exception("Не заданы объекты поля в файле уровня");
            throw ex;
        }
    }

    /**
     * * Метод разбора и установки одного типа игрового объектоа.
     *
     * Находит в json объекте данные об игровом объекте objClassName
     * и объект к объектам поля _field.
     *
     * @param obj json объект с информацией об игровом объекте.
     * @param objClassName имя класса игрового объекта.
     * @throws Exception при возникновении ошибок разбора.
     */
    private void parseObject(JSONObject obj,String objClassName) throws Exception {

        /* Парсинг объекта obj с типом, заданным именем objName */
        if( obj.containsKey("Positions")) {

            JSONArray pos = (JSONArray)obj.get("Positions");

            /* Позиции объектов данного типа */
            for(Object j : pos) {

                JSONArray onePos = (JSONArray) j;
                int x = Integer.parseInt(onePos.get(0).toString());
                int y = Integer.parseInt(onePos.get(1).toString());

                if(!_field.isPositionUnique(new Point(x,y))) {

                    String error = "Объект типа " + objClassName +
                                   " с позицией (" + onePos.get(0).toString() +
                                   ";" + onePos.get(1).toString() +
                                   ") не может быть размещен, "
                                   + "т.к. его позиция уже занята.";

                    throw new Exception(error);

                } else if((x < 1 || x > _field.getWidth()) ||
                          (y < 1 || y > _field.getHeight())) {

                    String error = "Объект типа " + objClassName +
                                   " с позицией (" + onePos.get(0).toString() +
                                   ";" + onePos.get(1).toString() +
                                   ") не может быть размещен, "
                                   + "т.к. его позиция за пределами поля.";

                    throw new Exception(error);
                }

                /* Создаем объект с помощью фабрики */
                GameObject object = new GameObjectsFactory().createGameObject(objClassName, getField());
                getField().addObject( new Point(x,y), object);

            }
        }
    }

    ////////////////////////////// Данные /////////////////////////////////////

    /** Максимальная ширина поля - клеток */
    private static final int MAX_WIDTH = 16;

    /** Максимальная высота поля - клеток */
    private static final int MAX_HEIGHT = 11;

    /** Количество ходов уровня */
    private int _moves = 100;

    /** Игровое поле уровня */
    private GameField _field;

    /** Имя уровня */
    private String _name = "default level name";

    /** История уровня */
    private String _history = "default level history";

    /** Путь к файлу с уровнем */
    private String _levelPath;

    /** Флаг - пройден ли уровень */
    private boolean _isCompleted = false;
}
