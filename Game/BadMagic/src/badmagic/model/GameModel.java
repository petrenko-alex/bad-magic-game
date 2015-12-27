package badmagic.model;

import badmagic.BadMagic;
import badmagic.events.GameObjectListener;
import badmagic.events.ModelListener;
import badmagic.model.Level;
import badmagic.model.gameobjects.GameObject;
import badmagic.model.gameobjects.Player;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GameModel {

    public GameModel() {

        loadLevels();
    }

    public void startNewCareer() {

        reset();
        loadLevels();

        /* Текущий уровень - первый */
        setCurrentLevel(0);

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
        _gameMode = GameMode.CAREER;
    }

    public void continueCareer() {

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
        _gameMode = GameMode.CAREER;
    }

    public void oneLevelMode(int levelNumber) {

        reset();
        loadLevels();

        /* Текущий уровень - выбранный */
        setCurrentLevel(levelNumber);

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
        _gameMode = GameMode.ONE_LEVEL;

    }

    public void nextLevel() {

        /* Текущий уровень - следующий */
        setCurrentLevel( ++_currentLevel );
        _lastCompletedLevel = _currentLevel;

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
    }

    public void tryAgain() {

        /* Загрузить уровень снова */
        Level sameLevel = new Level(_levelNames.get(_currentLevel));
        _levels.set(_currentLevel,sameLevel);

        /* Текущий уровень - этот же */
        setCurrentLevel( _currentLevel );

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
    }

    public GameField getField() {

        return _field;
    }

    public Player getPlayer() {

        return _player;
    }

    public LevelStatus getLevelStatus() {

        return _levelStatus;
    }

    public GameMode getGameMode() {

        return _gameMode;
    }

    public String getLevelName() {

        return _levels.get(_currentLevel).getName();
    }

    static public ArrayList<String> getLevelNames() {

        ArrayList<String> levelNames = new ArrayList<>();

        for( Level lvl : _levels ) {

            levelNames.add(lvl.getName());
        }

        return levelNames;
    }

    public int getMoves() {

        return _player.getMoves();
    }

    public boolean isLastLevel() {

        return (_levels.size() - 1) == _currentLevel;
    }

    private void loadLevels() {

        Level level = null;
        _levels.clear();

        /* Попытка загрузить имена файлов с уровнями */
        try {

            _levelNames = loadLevelNames();

        } catch (Exception ex) {

            ex.printStackTrace();
            return;
        }

        /* Загружаем данные уровней */
        for( Object i : _levelNames ) {

            level = new Level(i.toString());
            _levels.add(level);

        }
    }

    private void setCurrentLevel(int levelNumber) {

        if(levelNumber < 0 || levelNumber > (_levels.size() - 1) ) {

            return;
        }

        _currentLevel = levelNumber;
        _field = _levels.get(_currentLevel).getField();
    }

    private void initializePlayer() {

        /* Устанавливаем игрока */
        try {

            _player = (Player)_field.getObjects(
                    Class.forName("badmagic.model.gameobjects.Player")).get(0);
            _player.setMoves(_levels.get(_currentLevel).getMoves());
            _player.addObjectListener(_objectsObserver);

        } catch ( ClassNotFoundException ex ) {

            ex.printStackTrace();
        }
    }

    private void checkIfLevelIsFinished() {

        GameObject elixir = null;

        try {

            elixir = (GameObject)_field.getObjects(
                    Class.forName("badmagic.model.gameobjects.Elixir")).get(0);

        } catch ( ClassNotFoundException ex ) {

            ex.printStackTrace();
        }

        if( elixir != null ) {

            Point elixirPos = elixir.getPosition();
            Point playerPos = _player.getPosition();

            if( playerPos.equals(elixirPos) ) {

                fireLevelCompleted();
                _levelStatus = LevelStatus.COMPLETED;

            } else if( _player.getMoves() == 0 ){

                fireLevelFailed();
                _levelStatus = LevelStatus.FAILED;
            }
        }
    }

    public static void saveGameProgress() {

        JSONObject obj = new JSONObject();
        obj.put("LastCompletedLevel", _lastCompletedLevel);

        try (

            FileWriter file = new FileWriter(PATH_TO_GAME_PROGRESS_FILE)) {
            file.write(obj.toJSONString());

	} catch ( IOException ex ) {

            ex.printStackTrace();
        }
    }

    private void reset() {

        if( _field != null ) {

            _field.clear();
        }

        _player = null;
        _currentLevel = 0;
        _levels.clear();
        _levelNames.clear();
    }

    private ArrayList<String> loadLevelNames() throws Exception {

        ArrayList<String> levels = new ArrayList();
        JSONParser parser = new JSONParser();

        Object object = parser.parse(new FileReader(PATH_TO_LEVELS_INFO_FILE));
        JSONArray array = (JSONArray) object;

        if( array.isEmpty() ) {

            throw new Exception("Не заданы файлы уровней.");
        }

        for( Object i : array ) {

            levels.add(i.toString());
        }

        return levels;
    }

    private class ObjectsObserver implements GameObjectListener {

        @Override
        public void objectMoved(EventObject e) {

            checkIfLevelIsFinished();
        }
    }

    //////////////////////// События объектов /////////////////////////////////

    public void addObjectListener(GameObjectListener l) {

        _objectsListenerList.add(l);
    }

    public void removeObjectListener(GameObjectListener l) {

        _objectsListenerList.remove(l);
    }

    protected void fireObjectMoved(EventObject e) {

        for (Object listener : _objectsListenerList) {

            ((GameObjectListener) listener).objectMoved(e);
        }
    }

    private ArrayList _objectsListenerList = new ArrayList();

    ////////////////////////// События игры ///////////////////////////////////

    public void addModelListener(ModelListener l) {

        _modelListenerList.add(l);
    }

    public void removeModelListener(ModelListener l) {

        _modelListenerList.remove(l);
    }

    protected void fireLevelCompleted() {

        EventObject event = new EventObject(this);
        for(Object listener : _modelListenerList) {

            ((ModelListener)listener).levelCompleted(event);
        }
    }

    protected void fireLevelFailed() {

        EventObject event = new EventObject(this);
        for(Object listener : _modelListenerList) {

            ((ModelListener)listener).levelFailed(event);
        }
    }

    private ArrayList _modelListenerList = new ArrayList();

    //////////////////////////// Данные ///////////////////////////////////////

    private GameField _field;
    private int _currentLevel;
    private Player    _player;
    private GameMode _gameMode;
    private LevelStatus _levelStatus;
    private static int _lastCompletedLevel = 0;
    private static ArrayList<Level> _levels = new ArrayList();
    private ArrayList<String> _levelNames = new ArrayList();
    private ObjectsObserver _objectsObserver = new ObjectsObserver();
    private static final String PATH_TO_LEVELS_INFO_FILE =
                          "src/badmagic/resources/levels/levelsinfo.json";
    private static final String PATH_TO_GAME_PROGRESS_FILE =
                          "src/badmagic/resources/gameprogress.json";

    public enum LevelStatus {

    COMPLETED,
    FAILED,
    PLAYING
    }

    public enum GameMode {

    CAREER,
    ONE_LEVEL
    }
}
