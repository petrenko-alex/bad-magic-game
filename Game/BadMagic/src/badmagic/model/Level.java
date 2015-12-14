package badmagic.model;

import badmagic.BadMagic;
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
import java.util.Set;
     

public class Level {
    
    Level(String levelPath) {
        
        loadLevel(levelPath);
    }
     
    public void loadLevel(String levelPath) {
        
        Point size;
        JSONParser parser = new JSONParser();
        
        try {

            Object object = parser.parse(new FileReader(levelPath));
            JSONObject json = (JSONObject) object;
            
            /* Название уровня */
            if(json.containsKey("LevelName")) {
                
                _name = (String)json.get("LevelName");
            }
            
            /* История уровня */
            if(json.containsKey("LevelHistory")) {
                
                _history = (String)json.get("LevelHistory");
            }
            
            /* Количество ходов игрока */
            if(json.containsKey("PlayerMoves")) {
                
                _moves = Integer.parseInt(json.get("PlayerMoves").toString());
            }
            
            /* Размер поля */
            if( json.containsKey("FieldSize")) {
                
                JSONArray tmp = (JSONArray)json.get("FieldSize");
                int width = Integer.parseInt(tmp.get(0).toString());
                int height = Integer.parseInt(tmp.get(1).toString());
                _field = new GameField(width,height);
                
            } else {
                
                Exception e = new Exception( 
                                        "Не задан размер поля в файле уровня \"" 
                                        + levelPath + "\"");
                throw e;
            }
            
            /* Объекты поля */
            if( json.containsKey("FieldObjects")) {
                
                JSONArray objects = (JSONArray)json.get("FieldObjects");
                parseObjects(objects); 
                
            } else {
                
                Exception e = new Exception( 
                                      "Не заданы объекты поля в файле уровня \"" 
                                      + levelPath + "\"");
                
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            BadMagic.log.info("Ошика при чтении файда \"" + levelPath + "\"");
        }
    }

    public int getMoves() {
        
        return _moves;
    }

    public GameField getField() {
        
        return _field;
    }

    public String getName() {
        
        return _name;
    }

    public String getHistory() {
        
        return _history;
    }

    private void parseObjects(JSONArray objects) {
        
        /* Парсинг объектов в массиве objects */
        for(Object i : objects) {
            
            JSONObject obj = (JSONObject)i;
            Set keys = obj.keySet();
            
            /* 
             * Ключ - конкретный тип объекта, 
             * должен соответствовать имени класса 
             */
            for(Object key : keys) {
                
                JSONObject tmp = (JSONObject) obj.get(key.toString());
                parseObject(tmp,key.toString());
            }
        }
        
    }
    
    private void parseObject(JSONObject obj,String objClassName) {
        
        /* Парсинг объекта obj с типом, заданным именем objName */
        if( obj.containsKey("Positions")) {
                    
            JSONArray pos = (JSONArray)obj.get("Positions");
            
            /* Позиции объектов данного типа */
            for(Object j : pos) {
                        
                JSONArray onePos = (JSONArray) j;
                int x = Integer.parseInt(onePos.get(0).toString());
                int y = Integer.parseInt(onePos.get(1).toString());
                
                /* Создаем объект с помощью фабрики */
                GameObject object = new GameObjectsFactory().createGameObject(objClassName, getField());
                        
                getField().addObject( new Point(x,y), object);
                        
            }            
        }
    }
   
    ////////////////////////////// Данные /////////////////////////////////////
    private int _moves = 100;
    private GameField _field;
    private String _name = "default level name";
    private String _history = "default level history";
}
