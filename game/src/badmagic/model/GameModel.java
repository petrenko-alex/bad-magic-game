package badmagic.model;

import badmagic.BadMagic;
import badmagic.events.GameObjectListener;
import badmagic.events.ModelListener;
import badmagic.model.Level;
import badmagic.model.gameobjects.GameObject;
import badmagic.model.gameobjects.Player;
import java.awt.Point;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 * Класс представляет модель игры.
 *
 * Отвечает за логику игры: загрузку уровней,
 * режимы игры, переход между уровнями, определение завершенности уровня.
 * сохранение и загрузка информации о прохождении
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class GameModel {

    /**
     * Конструктор класса.
     *
     * Загружает уровни игры.
     *
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public GameModel() throws Exception {

        loadLevels();
    }

    /**
     * Метод начала новой игры в режиме карьеры.
     *
     * Загружает первый уровень и дальше игра продолжается
     * пока есть уровни.
     *
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public void startNewCareer() throws Exception {

        startLevel(0);
        _gameMode = GameMode.CAREER;
    }

    /**
     * Метод продожения игры в режиме карьеры
     *
     * Загружает из номер последнего пройденного уровня,
     * загружает этот уровень и дальше игра продолжается
     * пока есть уровни.
     *
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public void continueCareer() throws Exception {

        /* Попытка загрузить данные о прохождении */
        try {

            loadGameProgress();

        } catch ( Exception ex ) {

            ex.printStackTrace();
        }

        startLevel(_lastCompletedLevel);
        _gameMode = GameMode.CAREER;
    }

    /**
     * Метод начала игры в режиме одного уровня.
     *
     * Загружает выбранный уровень и дальше игра продолжается
     * пока есть уровни.
     *
     * @param levelNumber номер уровня
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public void oneLevelMode(int levelNumber) throws Exception {

        startLevel(levelNumber);
        _gameMode = GameMode.ONE_LEVEL;

    }

    /**
     * Метод перехода к следующему уровню игры.
     */
    public void nextLevel() {

        /* Текущий уровень - следующий */
        setCurrentLevel( ++_currentLevel );
        _lastCompletedLevel = _currentLevel;

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
    }

    /**
     * Метод повторного прохождения уровня.
     */
    public void tryAgain() {

        /* Загрузить уровень снова */
        Level sameLevel;
        try {

            sameLevel = new Level(_levelNames.get(_currentLevel));

        } catch ( Exception ex ) {

            ex.printStackTrace();
            return;
        }

        _levels.set(_currentLevel,sameLevel);

        /* Текущий уровень - этот же */
        setCurrentLevel( _currentLevel );

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
    }

    /**
     * Метод получения текущего игрового поля.
     *
     * @return GameField - текущее игровое поле.
     */
    public GameField getField() {

        return _field;
    }

    /**
     * Метод получения текущего игрока.
     *
     * @return Player - текущий игрок
     */
    public Player getPlayer() {

        return _player;
    }

    /**
     * Метод получения статуса текущего уровня.
     *
     * @return LevelStatus - статус уровня.
     */
    public LevelStatus getLevelStatus() {

        return _levelStatus;
    }

    /**
     * Метод получения текущего игрового режима.
     *
     * @return GameMode - текущий игровой режим
     */
    public GameMode getGameMode() {

        return _gameMode;
    }

    /**
     * Метод получения имени текущего уровня.
     *
     * @return String - имя текущего уровня.
     */
    public String getLevelName() {

        return _levels.get(_currentLevel).getName();
    }

    /**
     * Статический метод получения имен уровней игры.
     *
     * @return ArrayList - список имен уровней игры.
     */
    static public ArrayList<String> getLevelNames() {

        ArrayList<String> levelNames = new ArrayList<>();

        for( Level lvl : _levels ) {

            levelNames.add(lvl.getName());
        }

        return levelNames;
    }

    /**
     * Метод получения количества ходов текущего уровня.
     *
     * @return int - количество ходов.
     */
    public int getMoves() {

        return _player.getMoves();
    }

    /**
     * Метод,получения информации о том, является ли текущий уровень последним.
     *
     * @return boolean - флаг - последний ли уровень
     */
    public boolean isLastLevel() {

        return (_levels.size() - 1) == _currentLevel;
    }

    /**
     * Метод загрузки уровней игры.
     *
     * @throws Exception при возникновении ошибок загрузки.
     */
    private void loadLevels() throws Exception {

        Level level = null;
        _levels.clear();

        /* Попытка загрузить имена файлов с уровнями */
        _levelNames = loadLevelNames();

        /* Загружаем данные уровней */
        for( Object i : _levelNames ) {

            level = new Level(i.toString());
            _levels.add(level);

        }
    }

    /**
     * Метод начала уровня номер levelNumber.
     *
     * @param levelNumber номер уровня.
     * @throws Exception при возникновении ошибок загрузки уровня.
     */
    private void startLevel(int levelNumber) throws Exception {

        reset();
        loadLevels();

        /* Текущий уровень */
        setCurrentLevel(levelNumber);

        /* Инициализируем игрока */
        initializePlayer();

        /* Статус уровня */
        _levelStatus = LevelStatus.PLAYING;
    }

    /**
     * Метод установки уровня номер leveNumber как текущего.
     *
     * @param levelNumber номер уровня.
     */
    private void setCurrentLevel(int levelNumber) {

        if(levelNumber < 0 || levelNumber > (_levels.size() - 1) ) {

            return;
        }

        _currentLevel = levelNumber;
        _field = _levels.get(_currentLevel).getField();
    }

    /**
     * Метод инициализации текущего игрока.
     */
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

    /**
     * Метод проверки, завершен ли уровень.
     *
     * Проверяет, достиг ли игрок игрового объекта Elixir.
     * Если да - посылает сигнал об успешном прохождении уровня.
     * Если нет - проверяет оставшееся количество ходов и
     * если оно 0 - посылает сигнал о неуспешном прохождении уровня.
     */
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

    /**
     * Метод сохранения информации о последнем пройденном уровне в файл.
     */
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

    /**
     * Метод загрузки информации о последнем пройденном уровне из файла.
     *
     * @throws Exception при возникновении ошибок загрузки.
     */
    public void loadGameProgress() throws Exception {

        JSONParser parser = new JSONParser();

        Object object = parser.parse(new FileReader(PATH_TO_GAME_PROGRESS_FILE));
        JSONObject obj = (JSONObject)object;

        if( obj.containsKey("LastCompletedLevel") ) {

            String tmp = obj.get("LastCompletedLevel").toString();
            int value = Integer.parseInt(tmp);

            if( value > (_levels.size() - 1) ) {

                throw new Exception("Не корректно задана информация о "
                                    + "последнем пройденном уровне.");
            }
            _lastCompletedLevel = Integer.parseInt(tmp);

        } else {

            throw new Exception("Не задана информация о "
                                + "последнем пройденном уровне.");
        }
    }

    /**
     * Метод сброса модели.Очищает поля класса.
     */
    private void reset() {

        if( _field != null ) {

            _field.clear();
        }

        _player = null;
        _currentLevel = 0;
        _levels.clear();
        _levelNames.clear();
    }

    /**
     * Статический метод загрузки информации об уровнях игры.
     *
     * Загружает пути к файлам уровней из специального файла
     * с информацией об уровнях игры.
     *
     * @return ArrayList - список путей к файлам уровней.
     * @throws Exception при возникновении ошибок загрузки.
     */
    private ArrayList<String> loadLevelNames() throws Exception {

        ArrayList<String> levels = new ArrayList();
        JSONParser parser = new JSONParser();

        InputStream inputStream = getClass().getResourceAsStream(PATH_TO_LEVELS_INFO_FILE);
        Object object = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        JSONArray array = (JSONArray) object;

        if( array.isEmpty() ) {

            throw new Exception("Не заданы файлы уровней.");
        }

        for( Object i : array ) {

            levels.add(i.toString());
        }

        return levels;
    }

    /**
     * Внутренний класс - слушатель игровых объектов.
     *
     * Вызывает метод проверки, завершен ли уровень
     * при получении сигнала о том, что игровой объект перемещен.
     */
    private class ObjectsObserver implements GameObjectListener {

        /**
         * Метод, обрабатывающий получение сигнала о перемещении объекта.
         *
         * Проверяет, завершен ли уровень.
         *
         * @param e событие.
         */
        @Override
        public void objectMoved(EventObject e) {

            checkIfLevelIsFinished();
        }
    }

    ////////////////////////// События модели /////////////////////////////////

    /**
     * Метод добавления слушателя модели.
     * @param l слушатель.
     */
    public void addModelListener(ModelListener l) {

        _modelListenerList.add(l);
    }

    /**
     * Метод удаления слушаетля модели.
     * @param l слушатель.
     */
    public void removeModelListener(ModelListener l) {

        _modelListenerList.remove(l);
    }

    /**
     * Метод испускания сигнала об успешном прохождении уровня.
     */
    protected void fireLevelCompleted() {

        EventObject event = new EventObject(this);
        for(Object listener : _modelListenerList) {

            ((ModelListener)listener).levelCompleted(event);
        }
    }

    /**
     * Метод испускания сигнала о неуспешном прохождении уровня.
     */
    protected void fireLevelFailed() {

        EventObject event = new EventObject(this);
        for(Object listener : _modelListenerList) {

            ((ModelListener)listener).levelFailed(event);
        }
    }

    /** Список слушателей модели */
    private ArrayList _modelListenerList = new ArrayList();

    //////////////////////////// Данные ///////////////////////////////////////

    /** Игровое поле */
    private GameField _field;

    /** Номер текущего уровня */
    private int _currentLevel;

    /** Игрок */
    private Player _player;

    /** Текущий игровой режим */
    private GameMode _gameMode;

    /** Статус текущего уровня */
    private LevelStatus _levelStatus;

    /** Номер последнего пройденного уровня */
    private static int _lastCompletedLevel = 0;

    /** Список уровней игры */
    private static ArrayList<Level> _levels = new ArrayList();

    /** Список имен уровней игры */
    private ArrayList<String> _levelNames = new ArrayList();

    /** Слушатель игровых объектов */
    private ObjectsObserver _objectsObserver = new ObjectsObserver();

    /** Путь к файлу с информацией об уровнях */
    private static final String PATH_TO_LEVELS_INFO_FILE =
                          "/badmagic/resources/levels/levelsinfo.json";

    /** Путь к файлу с информацией о прогрессе игры */
    private static final String PATH_TO_GAME_PROGRESS_FILE =
                          "data/gameprogress.json";

    /** Статус уровня */
    public enum LevelStatus {

    COMPLETED,
    FAILED,
    PLAYING
    }

    /** Игровой режим */
    public enum GameMode {

    CAREER,
    ONE_LEVEL
    }
}
