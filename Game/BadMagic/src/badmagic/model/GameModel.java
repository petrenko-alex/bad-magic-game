package badmagic.model;

import badmagic.model.Level;
import java.util.ArrayList;

public class GameModel {
    
    public GameModel() {
        
        loadLevels();
    }
    
    public void loadLevels() {
        
        String levelFile = "src/badmagic/resources/levels/MysteryRectangle.json";
        
        Level level = new Level(levelFile);
        _levels.add(level);
        
        _field = level.getField();
        int a = 32;
    }
    
    public GameField getField() {
        
        return _field;
    }
    
    private GameField _field;
    private ArrayList<Level> _levels = new ArrayList();
    private static final String PATH_TO_LEVELS_INFO_FILE = 
                          "src/badmagic/resources/levels/levelsinfo.json";
    
}
