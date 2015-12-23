package badmagic.model;

import badmagic.events.GameObjectListener;
import badmagic.model.Level;
import badmagic.model.gameobjects.Player;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.logging.Logger;

public class GameModel {

    public GameModel() {

        loadLevels();
    }

    public void loadLevels() {

        String levelFile = "src/badmagic/resources/levels/MysteryRectangle.json";

        Level level = new Level(levelFile);
        _levels.add(level);

        _field = level.getField();

        try {

            _player = (Player)_field.getObjects(
                    Class.forName("badmagic.model.gameobjects.Player")).get(0);

        } catch ( ClassNotFoundException ex ) {

            ex.printStackTrace();
        }

    }

    public GameField getField() {

        return _field;
    }

    public Player getPlayer() {

        return _player;
    }

    private class ObjectsObserver implements GameObjectListener {

        @Override
        public void objectChanged(EventObject e) {

            /* Отправляем событие дальше - слушателям модели */
            fireObjectChanged(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public void addObjectListener(GameObjectListener l) {

        _listenerList.add(l);
    }

    public void removeObjectListener(GameObjectListener l) {

        _listenerList.remove(l);
    }

    protected void fireObjectChanged(EventObject e) {

        for (Object listener : _listenerList) {

            ((GameObjectListener) listener).objectChanged(e);
        }
    }

    private ArrayList _listenerList = new ArrayList();
    ///////////////////////////////////////////////////////////////////////////

    private GameField _field;
    private Player    _player;
    private ArrayList<Level> _levels = new ArrayList();
    private static final String PATH_TO_LEVELS_INFO_FILE =
                          "src/badmagic/resources/levels/levelsinfo.json";

}
