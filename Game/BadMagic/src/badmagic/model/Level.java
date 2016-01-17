package badmagic.model;

import badmagic.BadMagic;
import badmagic.model.gameobjects.Bookshelf;
import badmagic.model.gameobjects.GameObject;
import badmagic.model.gameobjects.WoodenTable;
import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import badmagic.model.gameobjects.GameObjectsFactory;
import badmagic.model.gameobjects.Spell;
import badmagic.model.gameobjects.Teleport;
import java.util.ArrayList;
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

        Object object = parser.parse(new FileReader(levelPath));
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
        
        /*Список позиций объекта типа objClassName*/
        ArrayList<Point>    positions = new ArrayList<>();
        /*Список идентификаторов заклинаний*/
        ArrayList<Integer>   spell_ids = new ArrayList<>();
        /*Список позиций для телепорта*/
        ArrayList<Point>    teleport_positions = new ArrayList<>();
        
        if( obj.containsKey("Positions")) {
            positions = parsePointArray((JSONArray)obj.get("Positions"));            
        }
        
        /*Для заклинаний*/
        /*Парсинг доступных id*/
        if( obj.containsKey("AvailableIds")) {
            spell_ids = parseIntegerArray((JSONArray)obj.get("AvailableIds"));
            getField().setSpellIdsList(spell_ids);
        }
        
        /*Парсинг заклинаний на поле и вне объектов*/
        if( obj.containsKey("OnField")) {
            /*несовпадение количества позиций и idшников на поле*/
            if (((JSONArray)obj.get("OnField")).size() != positions.size()){
                String error = "Количество заданных позиций для заклинаний не совпадает с перечнем заклинаний на поле";
                
                throw new Exception(error);
            }
            
            spell_ids.clear();
            
            for(Object j : (JSONArray)obj.get("OnField")) {

                int value = Integer.parseInt(j.toString());
               
                /* -1 - зарезервированный id для полок и телепортов без ключа*/
                if(_field.isSpellIdUnique(value) || value == -1) {

                    String error = "Неизвестный идентификатор заклинания (" + value +
                                   ") уже занят.";

                    throw new Exception(error);
                }
                
                spell_ids.add(value);
            }
                       
        }
        
        if (objClassName.contains("teleport") && positions.size() != teleport_positions.size()){
            /*Ошибка*/
        }
        
        for (int i = 0; i < positions.size(); ++i){
            GameObject o = new GameObjectsFactory().createGameObject(objClassName, getField());
            if (o instanceof Spell){
                /*Присваивание идентификтора*/
                ((Spell)o).setId(spell_ids.get(i));
            }
            if (o instanceof Bookshelf){
                /*Присваивание ключа и содержания*/
            }
            if (o instanceof Teleport){
                /*Присваивание ключа и позиции телепортирования*/
            }
            getField().addObject( positions.get(i), o);
        }
    }
    
    private ArrayList<Point>  parsePointArray (JSONArray array) throws Exception {
        
        ArrayList<Point> resultList = new ArrayList<>();
        
        for(Object j : array) {

                JSONArray positionData = (JSONArray) j;
                
                if (positionData.size()!=2){
                     String error = "Позиция " + (resultList.size()+1) + "-го элемента некорректна." 
                                    + "Ожидаемое количество координат: 2, Полученное количество: " +
                             positionData.size();

                    throw new Exception(error);
                }
                
                Point position = new Point(Integer.parseInt(positionData.get(0).toString()),
                                           Integer.parseInt(positionData.get(1).toString()));

                if(!_field.isPositionUnique(position)) {

                    String error = "Позиция " + (resultList.size()+1) 
                                    + "-го элемента (" + position.x +
                                   ";" + position.y +
                                   ") уже занята.";

                    throw new Exception(error);

                } else if((position.x < 1 || position.x > _field.getWidth()) ||
                          (position.y < 1 || position.y > _field.getHeight())) {

                    String error = "Позиция " + (resultList.size()+1) 
                                    + "-го элемента (" + position.x +
                                   ";" + position.y +
                                   ") не может быть размещен, "
                                   + "т.к. его позиция за пределами поля.";

                    throw new Exception(error);
                }
                
                resultList.add(position);
            }
        return resultList;
    }
    
    private ArrayList<Integer>  parseIntegerArray (JSONArray array) throws Exception {
        
        ArrayList<Integer> resultList = new ArrayList<>();
        
        for(Object j : array) {

                int value = Integer.parseInt(j.toString());
               
                /* -1 - зарезервированный id для полок и телепортов без ключа*/
                if(!_field.isSpellIdUnique(value) || value == -1) {

                    String error = "Идентификатор " + (resultList.size()+1) 
                                    + "-го заклинания (" + value +
                                   ") уже занят.";

                    throw new Exception(error);
                }
                
                resultList.add(value);
            }
        return resultList;
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
