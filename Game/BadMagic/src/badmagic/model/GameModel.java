package badmagic.model;

import badmagic.events.GameObjectListener;
import badmagic.events.ModelListener;
import badmagic.model.Level;
import badmagic.model.gameobjects.GameObject;
import badmagic.model.gameobjects.Player;
import java.awt.Point;
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
            _player.setMoves(level.getMoves());
            _player.addObjectListener(new ObjectsObserver());

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

            } else if( _player.getMoves() == 0 ){


                fireLevelFailed();
            }
        }
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
    ///////////////////////////////////////////////////////////////////////////

    private GameField _field;
    private Player    _player;
    private ArrayList<Level> _levels = new ArrayList();
    private static final String PATH_TO_LEVELS_INFO_FILE =
                          "src/badmagic/resources/levels/levelsinfo.json";

}
