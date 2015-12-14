package badmagic.model.gameobjects;

import badmagic.model.GameField;
import badmagic.BadMagic;
import java.lang.reflect.Constructor;


public class GameObjectsFactory {
    
    public GameObject createGameObject(String className,GameField field) {
        
        String tmpClassName = PATH_TO_GAME_OBJECTS_PACKAGE;
        tmpClassName = tmpClassName.concat(className);
        Object gameObject = null;
        
        try {
            
            Class c = Class.forName(tmpClassName);
            Constructor<?> constr = c.getConstructor(GameField.class);
            gameObject = constr.newInstance(field);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            BadMagic.log.info("Не удалось найти класс с указанным именем");
        }
        
        return (GameObject)gameObject;
    }
    
    private final static String PATH_TO_GAME_OBJECTS_PACKAGE = 
                                "badmagic.model.gameobjects.";
}
