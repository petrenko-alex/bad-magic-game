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
import java.awt.FontMetrics;
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Класс представляет игровую панель.
 *
 * Отображает текущее игровое поле, показывает перемещения объектов,
 * информацию о текущем уровне, выводит информирующие сообщения
 * при прохождении уровней, сожержит элементы управления - кнопки.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class GamePanel extends JPanel {

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Настраивает размеры и вид панели.
     *
     * @param model игровая модель.
     */
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

    /**
     * Метод начала новой игры в режиме карьеры.
     *
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public void startNewCareer() throws Exception {

        _model.startNewCareer();
    }

    /**
     * Метод продолжения игры в режиме карьеры.
     *
     * @throws Exception при возникновении ошибок загрузки уровней.
     */
    public void continueCareer() throws Exception {

        _model.continueCareer();
    }

    /**
     * Метод начала новой игры в режиме прохождения одного уровня.
     *
     * @param levelNumber номер уровня.
     * @throws Exception при возникновении ошибок загрузки уровня.
     */
    public void oneLevelMode(int levelNumber) throws Exception {

        _model.oneLevelMode(levelNumber);
    }

    /**
     * Метод отключения слушателей мыши и клавиатуры.
     */
    public void stopListenToPeriphery() {

        removeKeyListener(_keyHandler);
        removeMouseListener(_clickListener);
    }

    /**
     * Метод подключения слушателей мыши и клавиатуры.
     */
    public void startListenToPeriphery() {

        addKeyListener(_keyHandler);
        addMouseListener(_clickListener);
    }

    /**
     * Метод отрисовки компонентов панели.
     *
     * @param g среда отрисовки.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        paintInfoPanel(g);
        if (_model.getLevelStatus() == GameModel.LevelStatus.PLAYING) {

            paintGrid(g);
            paintObjects(g);

        } else {

            paintResultBox(g);
        }
    }

    /**
     * Метод отрисовки информационной панели.
     *
     * @param g среда отрисовки.
     */
    private void paintInfoPanel(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int width = BadMagic.getWindowWidth();
        int height = BadMagic.getWindowHeight();
        BasicStroke borders = new BasicStroke(OBJECTS_BORDER_WIDTH);
        g2d.setStroke(borders);

        setBackground(BACKGROUND_COLOR);
        g.drawImage(_infoPanel, -1, -1, null);

        g.setFont(_levelFont);
        g.drawString("\"" + _model.getLevelName() + "\"", 15, 50);
        g.drawString("Осталось ходов: \n" + _model.getMoves(), 5, 94);

        //g.setColor(OBJECTS_BORDER_COLOR);
        g.drawString("Управление: ", 52, 420);
        g.drawString("Перемещение:", 46, 445);
        g.setFont(_simpleFont);
        g.drawString("\u2190, \u2191, \u2192, \u2193", 50, 470);
        g.setFont(_levelFont);
        g.drawString("Сдвинуть стол:", 44, 495);
        g.drawString("Ctrl+", 26, 520);
        g.setFont(_simpleFont);
        g.drawString("\u2190, \u2191, \u2192, \u2193", 76, 520);

        g2d.draw(_quitGameBtn);
        g.setColor(FONT_COLOR);
        g.setFont(_levelFont);
        g.drawString("Выход", 70, 670);
    }

    /**
     * Метод отрисовки сетки игрового поля.
     *
     * @param g среда отрисовки.
     */
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
        g.drawRect(_fieldStartX, _fieldStartY, width, height);

        /* Текстуры */
        for (int i = 1; i < (rows + 1); i++) {

            int y = _fieldStartY + CELL_SIZE * (i - 1);
            for (int j = 1; j < (columns + 1); j++) {

                int x = _fieldStartX + CELL_SIZE * (j - 1);
                g.drawImage(_cellPic, x, y, null);
            }
        }

        /* Сетка */
        /* Вертикальные линии */
        for (int i = 1; i <= (columns + 1); i++) {
            int x1 = _fieldStartX + CELL_SIZE * (i - 1);
            g.drawLine(x1, _fieldStartY, x1, (_fieldStartY + height));
        }

        /* Горизонтальные линии */
        for (int i = 1; i <= (rows + 1); i++) {
            int y1 = _fieldStartY + CELL_SIZE * (i - 1);
            g.drawLine(_fieldStartX, y1, (_fieldStartX + width), y1);
        }
    }

    /**
     * Метод отрисовки блока результатов прохождения уровня.
     *
     * @param g среда отрисовки.
     */
    private void paintResultBox(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font(FONT_TYPE, Font.BOLD, 20);
        g.setFont(font);
        int boxY = (BadMagic.getWindowHeight() - RESULT_BOX_HEIGHT) / 2;
        int boxX = ((BadMagic.getWindowWidth() - RESULT_BOX_WIDTH) / 2)
                + (INFO_PANEL_WIDTH / 2);

        Rectangle levelFinishedBox = new Rectangle(
                boxX, boxY, RESULT_BOX_WIDTH, RESULT_BOX_HEIGHT);
        g2d.draw(levelFinishedBox);

        /* Кнопка следующего действия */
        _nextActionBtn = new Rectangle(boxX + 60, boxY + 200, 220, 40);

        /* Кнопка перехода в главное меню */
        if (_model.getLevelStatus() == GameModel.LevelStatus.COMPLETED
                && _model.getGameMode() == GameModel.GameMode.CAREER
                && _model.isLastLevel()) {

            int xPos = (RESULT_BOX_WIDTH - 215) / 2;
            _mainMenuBtn = new Rectangle(boxX + xPos, boxY + 200,
                    RES_BOX_BUTTON_WIDTH,
                    RES_BOX_BUTTON_HEIGHT);

            paintCenteredString(g, _mainMenuBtn, "Главное меню",
                                _mainMenuBtn.y, _mainMenuBtn.x);

        } else {

            _mainMenuBtn = new Rectangle(boxX + 310, boxY + 200,
                    RES_BOX_BUTTON_WIDTH,
                    RES_BOX_BUTTON_HEIGHT);

            paintCenteredString(g, _mainMenuBtn, "Главное меню",
                                _mainMenuBtn.y, _mainMenuBtn.x);

        }
        g2d.draw(_mainMenuBtn);

        /* Кнопка следующего действия */
        if (_model.getLevelStatus() == GameModel.LevelStatus.COMPLETED) {

            /* Если уровень успешно пройден */
            paintCenteredString( g, levelFinishedBox, "Поздравляем!",
                                 boxY - 120, boxX);

            paintCenteredString( g, levelFinishedBox, "Вы успешно прошли уровень "
                    + _model.getLevelName() + "!", boxY - 90, boxX);


            if (_model.getGameMode() == GameModel.GameMode.CAREER
                    && !_model.isLastLevel()) {

                g2d.draw(_nextActionBtn);
                paintCenteredString( g, _nextActionBtn, "Следующий уровень",
                                     _nextActionBtn.y, _nextActionBtn.x);

            } else if (_model.getGameMode() == GameModel.GameMode.ONE_LEVEL) {

                g2d.draw(_nextActionBtn);
                paintCenteredString( g, _nextActionBtn, "Попробовать снова",
                                     _nextActionBtn.y, _nextActionBtn.x);
            }

        } else if (_model.getLevelStatus() == GameModel.LevelStatus.FAILED) {

            /* Если уровень провален */
            paintCenteredString(g, levelFinishedBox, "Увы:(", boxY - 120, boxX);
            paintCenteredString(g, levelFinishedBox, "Вам не удалось пройти уровень "
                                + _model.getLevelName() + "!", boxY - 90, boxX);


            g2d.draw(_nextActionBtn);
            paintCenteredString(g, _nextActionBtn, "Попробовать снова",
                                _nextActionBtn.y, _nextActionBtn.x);
        }
    }

    /**
     * Метод отрисовки игровых объектов поля.
     *
     * @param g среда отрисовки.
     */
    private void paintObjects(Graphics g) {

        for (GameObject obj : _model.getField().getObjects()) {

            Point objPos = obj.getPosition();
            objPos = getPanelPosition(objPos);
            obj.paint(g, objPos);
        }
    }

    /**
     * Метод отрисоки строки посередине прямоугольной области.
     *
     * @param g среда отрисоки.
     * @param area область,посередине которой отрисовывается строка.
     * @param string строка.
     * @param yStart смещение области в панели по y.
     * @param xStart смещение области в панели по x.
     */
    private void paintCenteredString(Graphics g,Rectangle area,String string,
                                     int yStart,int xStart) {

        /* Настройки шрифта */
        FontMetrics fm   = g.getFontMetrics(g.getFont());
        Rectangle2D rect = fm.getStringBounds(string, g);

        /* Размеры */
        int textHeight = (int)(rect.getHeight());
        int textWidth  = (int)(rect.getWidth());
        int panelHeight= (int)(area.getHeight());
        int panelWidth = (int)(area.getWidth());

        /* Координаты начала */
        int x = (panelWidth  - textWidth)  / 2;
        int y = (panelHeight - textHeight) / 2  + fm.getAscent();

        g.drawString(string, x + xStart, y + yStart);
    }

    /**
     * Метод загрузки ресурсов панели.
     */
    private void loadResources() {

        try {

            _cellPic = ImageIO.read(getClass().getResource(PIC));
            _infoPanel = ImageIO.read(getClass().getResource(INFO_PANEL));
            _levelFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResource(LEVEL_FONT_PATH).openStream()).deriveFont(FONT_SIZE);
            _simpleFont = new Font(FONT_TYPE, 0, 24);

        } catch (IOException ex) {

            ex.printStackTrace();

        } catch (FontFormatException ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Метод получения координат панели для отрисовки по логическим координатам.
     *
     * @param logPos лигические координаты.
     * @return координаты панели для отрисовки.
     */
    private Point getPanelPosition(Point logPos) {

        int x = _fieldStartX + (CELL_SIZE * (logPos.x - 1));
        int y = _fieldStartY + (CELL_SIZE * (logPos.y - 1));
        return new Point(x, y);
    }

    //////////////////////////// Слушатели ////////////////////////////////////

    /**
     * Внутренний класс - слушатель событий модели.
     */
    private class ModelObserver implements ModelListener {

        /**
         * Метод, обрабатывающий сигнал об успешном прохождении уровня.
         *
         * @param e событие.
         */
        @Override
        public void levelCompleted(EventObject e) {

            BadMagic.log.info("Found elixir. Level finished.");
            removeKeyListener(_keyHandler);
        }

        /**
         * Метод, обрабатывающий сигнал о неуспешном прохождении уровня.
         *
         * @param e событие.
         */
        @Override
        public void levelFailed(EventObject e) {

            BadMagic.log.info("Moves left. Level failed.");
            removeKeyListener(_keyHandler);
        }

    }

    /**
     * Внутренний класс - слушатель клавиатуры.
     */
    private class KeyHandler extends KeyAdapter {

        /**
         * Метод, обрабатывающий нажатие клавиш клавиатуры.
         * @param _ke
         */
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

                        BadMagic.log.info("Use object");
                        break;
                    }
                    default: {

                        break;
                    }
                }
            }

        }

    }

    /**
     * Внутренний класс - слушатель мыши.
     */
    private class ClickListener extends MouseAdapter {

        /**
         * Метод, обрабатывающий клики мыши.
         * @param e событие.
         */
        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            /* Выйти из игры */
            if (x >= _quitGameBtn.x
                    && x <= (_quitGameBtn.x + _quitGameBtn.width)) {

                if (y >= _quitGameBtn.y
                        && y <= (_quitGameBtn.y + _quitGameBtn.height)) {

                    GameModel.saveGameProgress();
                    System.exit(0);
                }
            }

            /* Блок результатов */
            if (_model.getLevelStatus() != GameModel.LevelStatus.PLAYING) {

                /* Главное меню */
                if (x >= _mainMenuBtn.x
                        && x <= (_mainMenuBtn.x + _mainMenuBtn.width)) {

                    if (y >= _mainMenuBtn.y
                            && y <= (_mainMenuBtn.y + _mainMenuBtn.height)) {

                        fireMainMenuClicked();
                        GameModel.saveGameProgress();
                    }
                }

                if (x >= _nextActionBtn.x
                        && x <= (_nextActionBtn.x + _nextActionBtn.width)) {

                    if (y >= _nextActionBtn.y
                            && y <= (_nextActionBtn.y + _nextActionBtn.height)) {

                        if (_model.getLevelStatus() == GameModel.LevelStatus.COMPLETED
                                && _model.getGameMode() == GameModel.GameMode.CAREER
                                && !_model.isLastLevel()) {

                            _model.nextLevel();
                            stopListenToPeriphery();
                            startListenToPeriphery();

                        } else if (_model.getLevelStatus() == GameModel.LevelStatus.FAILED
                                || _model.getGameMode() == GameModel.GameMode.ONE_LEVEL) {

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

    /**
     * Метод добавления слушателя панели.
     * @param l слушатель.
     */
    public void addPanelListener(PanelListener l) {

        _listenerList.add(l);
    }

    /**
     * Метод удаления слушателя панели.
     * @param l слушатель.
     */
    public void removePanelListener(PanelListener l) {

        _listenerList.remove(l);
    }

    /**
     * Метод испускания сигнала о нажатии кнопки "Главное меню".
     */
    protected void fireMainMenuClicked() {

        EventObject e = new EventObject(this);
        for (Object listener : _listenerList) {

            ((PanelListener) listener).mainMenuClicked(e);
        }
    }

    /** Список слушателей панели */
    private ArrayList _listenerList = new ArrayList();

    //////////////////////////// Данные ///////////////////////////////////////

    /** Изображение клетки */
    private BufferedImage _cellPic;

    /** Изображение информационной панели */
    private BufferedImage _infoPanel;

    /** Шрифт уровня */
    private static Font _levelFont;

    /** Шрифт кнопок */
    private static Font _buttonFont;

    /** Обычный шрифт */
    private static Font _simpleFont;

    /** Игровая модель */
    private GameModel _model;

    /** Координата начала отрисовки игрового поля по Х */
    private int _fieldStartX;

    /** Координата начала отрисовки игрового поля по Y */
    private int _fieldStartY;

    /** "Кнопка" перехода в главное меню */
    private static Rectangle _mainMenuBtn;

    /** "Кнопка" следующего действия при прохождении уровня */
    private static Rectangle _nextActionBtn;

    /** Слушатель клавиатуры */
    private KeyHandler _keyHandler = new KeyHandler();

    /** Слушатель мыши */
    private ClickListener _clickListener = new ClickListener();

    //////////////////////////// Константы ////////////////////////////////////

    /** Размер клетки */
    private static final int CELL_SIZE = 64;

    /** Путь к файлу с изображением клетки */
    private static final String PIC = "/badmagic/resources/brick.png";

    /** Путь к файлу с изображением информационной панели */
    private static final String INFO_PANEL = "/badmagic/resources/panel.png";

    /** Путь к шрифту */
    private static final String LEVEL_FONT_PATH = "/badmagic/resources/level.ttf";

    /** Цвет фона */
    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);

    /** Цвет рамок */
    private static final Color OBJECTS_BORDER_COLOR = new Color(205, 133, 63);

    /** Размер шрифта */
    private static final float FONT_SIZE = 24;

    /** Тип шрифта */
    private static final String FONT_TYPE = "Times New Roman";

    /** Цвет шрифта */
    private static final Color FONT_COLOR = new Color(205, 133, 63);

    /** Ширина кнопки */
    private static final int BUTTON_WIDTH = 70;

    /** Высота кнопки */
    private static final int BUTTON_HEIGHT = 30;

    /** Ширина информационной панели */
    private static final int INFO_PANEL_WIDTH = 200;

    /** Ширина бока результатов */
    private static final int RESULT_BOX_WIDTH = 600;

    /** Высота блока результатов */
    private static final int RESULT_BOX_HEIGHT = 300;

    /** Ширина кнопок блока результатов */
    private static final int RES_BOX_BUTTON_WIDTH = 215;

    /** Высота кнопок блока результатов */
    private static final int RES_BOX_BUTTON_HEIGHT = 40;

    /** Ширина рамки */
    private static final int OBJECTS_BORDER_WIDTH = 2;

    /** "Кнопка" выхода из игры */
    private static final Rectangle _quitGameBtn
            = new Rectangle(60, 650, BUTTON_WIDTH, BUTTON_HEIGHT);

}
