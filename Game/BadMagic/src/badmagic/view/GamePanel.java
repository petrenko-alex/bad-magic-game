package badmagic.view;

import badmagic.model.GameModel;
import badmagic.BadMagic;
import badmagic.events.GameObjectListener;
import badmagic.events.ModelListener;
import badmagic.events.PanelListener;
import badmagic.model.gameobjects.GameObject;
import badmagic.navigation.Direction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    public GamePanel(GameModel model) {

        super();
        setPreferredSize(new Dimension(BadMagic.getWindowWidth(),
                                       BadMagic.getWindowHeight()));
        setFocusable(true);
        requestFocus();

        _model = model;
        _model.addModelListener(new ModelObserver());
        loadResources();
    }

    public void startNewCareer() {

        _model.startNewCareer();
    }

    public void continueCareer() {

        _model.continueCareer();
    }

    public void oneLevelMode(int levelNumber) {

        _model.oneLevelMode(levelNumber);
    }

    public void stopListenToPeriphery() {

        removeKeyListener(_keyHandler);
        removeMouseListener(_clickListener);
    }

    public void startListenToPeriphery() {

        addKeyListener(_keyHandler);
        addMouseListener(_clickListener);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        paintInfoPanel(g);
        if(_model.getLevelStatus() == GameModel.LevelStatus.PLAYING) {

            paintGrid(g);
            paintObjects(g);

        } else {

            paintResultBox(g);
        }
    }

    private void paintInfoPanel(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int width = BadMagic.getWindowWidth();
        int height = BadMagic.getWindowHeight();
        BasicStroke borders = new BasicStroke(OBJECTS_BORDER_WIDTH);
        g2d.setStroke(borders);

        setBackground(BACKGROUND_COLOR);
        //g.setColor(OBJECTS_BORDER_COLOR);
        g.drawImage(_infoPanel, -1, -1, null);

        g.setFont(_levelFont);
        g.drawString("\""+_model.getLevelName()+"\"" , 15, 45);
        g.drawString("Осталось ходов: " + _model.getMoves(), 5, 85);

        
        g2d.draw(_quitGameBtn);
        g.setColor(FONT_COLOR);
        g.drawString("Выход", 70, 670);
    }

    private void paintGrid(Graphics g) {

        /* Координаты и размеры */
        int rows = _model.getField().getHeight();
        int columns = _model.getField().getWidth();
        int height = rows * CELL_SIZE;
        int width = columns * CELL_SIZE;

        int areaWidth = BadMagic.getWindowWidth() - INFO_PANEL_WIDTH;
        int areaHeight = BadMagic.getWindowHeight();
        int xOffset = (areaWidth - width) / 2;
        int yOffset = (areaHeight - height) / 2;

        _fieldStartX = INFO_PANEL_WIDTH + xOffset;
        _fieldStartY = yOffset;

        /* Обрамляющий прямоугольник */
        g.drawRect(_fieldStartX,_fieldStartY,width,height);

        /* Текстуры */
        for(int i = 1; i < (rows + 1);i++) {

            int y = _fieldStartY + CELL_SIZE * (i-1);
            for(int j = 1; j < (columns + 1);j++) {

                int x = _fieldStartX + CELL_SIZE * (j - 1);
                g.drawImage(_cellPic, x, y, null);
            }
        }

        /* Сетка */
        /* Вертикальные линии */
        for(int i = 1; i <= (columns + 1); i++)
        {
            int x1 = _fieldStartX + CELL_SIZE * (i - 1);
            g.drawLine(x1, _fieldStartY, x1, (_fieldStartY + height));
        }

        /* Горизонтальные линии */
        for(int i = 1; i <= (rows + 1); i++)
        {
            int y1 = _fieldStartY + CELL_SIZE * (i - 1);
            g.drawLine(_fieldStartX, y1, (_fieldStartX + width), y1);
        }
    }

    private void paintResultBox(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;
        Font font = new Font(FONT_TYPE, Font.BOLD, 20);
        g.setFont(font);
        int boxY = (BadMagic.getWindowHeight() - RESULT_BOX_HEIGHT) / 2;
        int boxX = ((BadMagic.getWindowWidth() - RESULT_BOX_WIDTH) / 2) +
                   (INFO_PANEL_WIDTH / 2);

        Rectangle levelFinishedBox = new Rectangle(
                                boxX,boxY, RESULT_BOX_WIDTH, RESULT_BOX_HEIGHT);
        g2d.draw(levelFinishedBox);

        /* Кнопка следующего действия */
        _nextActionBtn = new Rectangle(boxX + 60,boxY + 200,220,40);

        /* Кнопка перехода в главное меню */
        if( _model.getLevelStatus() == GameModel.LevelStatus.COMPLETED &&
            _model.getGameMode() == GameModel.GameMode.CAREER &&
            _model.isLastLevel() ) {

            int xPos = (RESULT_BOX_WIDTH - 215) / 2;
            _mainMenuBtn = new Rectangle(   boxX + xPos,boxY + 200,
                                            RES_BOX_BUTTON_WIDTH,
                                            RES_BOX_BUTTON_HEIGHT);
            g.drawString("Главное меню", boxX + xPos + 35, boxY + 225);

        } else {

            _mainMenuBtn = new Rectangle(   boxX + 310,boxY + 200,
                                            RES_BOX_BUTTON_WIDTH,
                                            RES_BOX_BUTTON_HEIGHT);
            g.drawString("Главное меню", boxX + 345, boxY + 225);
        }
        g2d.draw(_mainMenuBtn);

        /* Кнопка следующего действия */
        if( _model.getLevelStatus() == GameModel.LevelStatus.COMPLETED ) {

            /* Если уровень успешно пройден */
            g.drawString("Поздравляем!", boxX + 240, boxY + 30);
            g.drawString("Вы успешно прошли уровень " +
                       _model.getLevelName() + "!", boxX + 60, boxY + 60);

            if( _model.getGameMode() == GameModel.GameMode.CAREER &&
                !_model.isLastLevel() ) {

                g2d.draw(_nextActionBtn);
                g.drawString("Следующий уровень", boxX + 70, boxY + 225);

            } else if( _model.getGameMode() == GameModel.GameMode.ONE_LEVEL ) {

                g2d.draw(_nextActionBtn);
                g.drawString("Попробовать снова", boxX + 75, boxY + 225);
            }

        } else if ( _model.getLevelStatus() == GameModel.LevelStatus.FAILED ) {

            /* Если уровень провален */
            g.drawString("Увы :(", boxX + 260, boxY + 30);
            g.drawString("Вам не удалось пройти уровень " +
                       _model.getLevelName() + "!", boxX + 40, boxY + 60);

            g2d.draw(_nextActionBtn);
            g.drawString("Попробовать снова", boxX + 75, boxY + 225);
        }
    }

    private void paintObjects(Graphics g) {

        for(GameObject obj : _model.getField().getObjects()) {

            Point objPos = obj.getPosition();
            objPos = getPanelPosition(objPos);
            obj.paint(g, objPos);
        }
    }

    private void loadResources() {

        try {

            _cellPic = ImageIO.read(getClass().getResource(PIC));
            _infoPanel = ImageIO.read(getClass().getResource(INFO_PANEL));
             _levelFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResource(LEVEL_FONT_PATH).openStream()).deriveFont(FONT_SIZE);
        } catch ( IOException ex ) {

            ex.printStackTrace();
        } catch ( FontFormatException ex ) {

            ex.printStackTrace();
        }
    }

    private Point getPanelPosition(Point logPos) {

        int x = _fieldStartX + (CELL_SIZE * (logPos.x - 1));
        int y = _fieldStartY + (CELL_SIZE * (logPos.y - 1));
        return new Point(x,y);
    }

    //////////////////////////// Слушатели ////////////////////////////////////

    private class ObjectsObserver implements GameObjectListener {

        @Override
        public void objectMoved(EventObject e) {

            ((GameObject)e.getSource()).paint(getGraphics(), null);
        }
    }

    private class ModelObserver implements ModelListener {

        @Override
        public void levelCompleted(EventObject e) {

            BadMagic.log.info("Элексир достигнут. Уровень пройден.");
            removeKeyListener(_keyHandler);
        }

        @Override
        public void levelFailed(EventObject e) {

            BadMagic.log.info("Закончились ходы. Уровень провален.");
            removeKeyListener(_keyHandler);
        }

    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent _ke) {

            int key = _ke.getKeyCode();
            if (_ke.isControlDown()) {

                /* Переместить объект */
                switch (_ke.getKeyCode()) {

                    case (KeyEvent.VK_LEFT): {

                        _model.getPlayer().moveObject(Direction.west());
                        break;
                    }
                    case (KeyEvent.VK_RIGHT): {

                        _model.getPlayer().moveObject(Direction.east());
                        break;
                    }
                    case (KeyEvent.VK_UP): {

                        _model.getPlayer().moveObject(Direction.north());
                        break;
                    }
                    case (KeyEvent.VK_DOWN): {

                        _model.getPlayer().moveObject(Direction.south());
                        break;
                    }
                    default: {

                        break;
                    }
                }
            } else {

                /* Переместить игрока */
                switch (_ke.getKeyCode()) {

                    case (KeyEvent.VK_LEFT): {

                        _model.getPlayer().move(Direction.west());
                        break;
                    }
                    case (KeyEvent.VK_RIGHT): {

                        _model.getPlayer().move(Direction.east());
                        break;
                    }
                    case (KeyEvent.VK_UP): {

                        _model.getPlayer().move(Direction.north());
                        break;
                    }
                    case (KeyEvent.VK_DOWN): {

                        _model.getPlayer().move(Direction.south());
                        break;
                    }
                    case (KeyEvent.VK_SPACE): {

                        BadMagic.log.info("Активировать объект");
                        break;
                    }
                    default: {

                        break;
                    }
                }
            }

        }

    }

    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            /* Выйти из игры */
            if ( x >= _quitGameBtn.x &&
                 x <= (_quitGameBtn.x + _quitGameBtn.width) ) {

                if ( y >= _quitGameBtn.y &&
                     y <= (_quitGameBtn.y + _quitGameBtn.height) ) {

                    GameModel.saveGameProgress();
                    System.exit(0);
                }
            }

            /* Блок результатов */
            if( _model.getLevelStatus() != GameModel.LevelStatus.PLAYING ) {

                /* Главное меню */
                if ( x >= _mainMenuBtn.x &&
                     x <= (_mainMenuBtn.x + _mainMenuBtn.width) ) {

                    if( y >= _mainMenuBtn.y &&
                        y <= (_mainMenuBtn.y + _mainMenuBtn.height) ) {

                        fireMainMenuClicked();
                    }
                }

                if( x >= _nextActionBtn.x &&
                    x <= (_nextActionBtn.x + _nextActionBtn.width) ) {

                    if( y >= _nextActionBtn.y &&
                        y <= (_nextActionBtn.y + _nextActionBtn.height)) {

                        if( _model.getLevelStatus() == GameModel.LevelStatus.COMPLETED &&
                            _model.getGameMode() == GameModel.GameMode.CAREER &&
                            !_model.isLastLevel()) {

                            _model.nextLevel();
                            stopListenToPeriphery();
                            startListenToPeriphery();

                        } else if( _model.getLevelStatus() == GameModel.LevelStatus.FAILED ||
                                   _model.getGameMode() == GameModel.GameMode.ONE_LEVEL ) {

                            _model.tryAgain();
                            stopListenToPeriphery();
                            startListenToPeriphery();
                        }
                    }
                }
            }
    }
     }

    //////////////////////////// Сигналы //////////////////////////////////////

    private ArrayList _listenerList = new ArrayList();

    public void addPanelListener(PanelListener l) {

        _listenerList.add(l);
    }

    public void removePanelListener(PanelListener l) {

        _listenerList.remove(l);
    }

    protected void fireMainMenuClicked() {

        EventObject e = new EventObject(this);
        for( Object listener : _listenerList ) {

            ((PanelListener)listener).mainMenuClicked(e);
        }
    }

    //////////////////////////// Данные ///////////////////////////////////////

    private BufferedImage _cellPic;
    private BufferedImage _infoPanel;
    private static Font _levelFont;
    private GameModel _model;
    private int _fieldStartX;
    private int _fieldStartY;
    private static Rectangle _mainMenuBtn;
    private static Rectangle _nextActionBtn;
    private KeyHandler _keyHandler = new KeyHandler();
    private ClickListener _clickListener = new ClickListener();

    //////////////////////////// Константы ////////////////////////////////////

    private static final int CELL_SIZE = 64;
    private static final String PIC = "/badmagic/resources/brick.png";
    private static final String INFO_PANEL = "/badmagic/resources/panel.png";
    private static final String LEVEL_FONT_PATH = "/badmagic/resources/level.ttf";
    

    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);
    private static final Color OBJECTS_BORDER_COLOR = new Color(205, 133, 63);

    private static final float FONT_SIZE = 24;
    private static final String FONT_TYPE = "Comic Sans MS";
    private static final Color FONT_COLOR = new Color(205, 133, 63);

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 30;
    private static final int INFO_PANEL_WIDTH = 200;
    private static final int RESULT_BOX_WIDTH = 600;
    private static final int RESULT_BOX_HEIGHT = 300;
    private static final int RES_BOX_BUTTON_WIDTH = 215;
    private static final int RES_BOX_BUTTON_HEIGHT = 40;
    private static final int OBJECTS_BORDER_WIDTH = 2;

    private static final Rectangle _quitGameBtn
                      = new Rectangle(60,650, BUTTON_WIDTH, BUTTON_HEIGHT);


}
