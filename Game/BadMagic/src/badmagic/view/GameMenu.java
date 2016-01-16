package badmagic.view;

import badmagic.BadMagic;
import badmagic.events.MenuEvent;
import badmagic.events.MenuListener;
import badmagic.model.GameModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Класс представляет игровое меню.
 *
 * Позволяет начать новую игру в режиме карьеры,
 * продолжить текущую игру, пройти конкретный уровень.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class GameMenu extends JPanel {

    /**
     * Конструктор класса.
     *
     * Настраивает размеры и вид панели.
     */
    public GameMenu() {

        super();

        setPreferredSize(new Dimension(BadMagic.getWindowWidth(),
                                       BadMagic.getWindowHeight()));
        setFocusable(true);
        requestFocus();
    }

    /**
     * Метод отрисовки компонентов панели.
     *
     * @param g среда отрисовки.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        /* Вывод фонового изображения */
        Image menu_background = null;
        Font menu_font = null;

        try {

            /*Загрузка изображения*/
            menu_background = ImageIO.read(this.getClass().getResource(MENU_BACKGROUND_PATH));
            /*Загрузка шрифта*/
            menu_font = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResource(MENU_FONT_PATH).openStream()).deriveFont(FONT_SIZE);
            /*Регистрация шрифта*/
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(menu_font);

        } catch ( IOException e ) {

            e.printStackTrace();

        } catch ( FontFormatException e ) {

            e.printStackTrace();
        }

        //Установка изображения
        g.drawImage(menu_background.getScaledInstance(BadMagic.getWindowWidth(), BadMagic.getWindowHeight(), Image.SCALE_DEFAULT), 0, 0, this);
        //Установка шрифта
        g.setFont(menu_font);
        g.setColor(FONT_COLOR);

        /* Кнопки меню в зависимости от типа */
        if( _menuMode == MenuMode.MAIN_MENU ) {

            paintMainMenu(g);

        } else if ( _menuMode ==MenuMode.LEVEL_MENU ) {

            paintLevelMenu(g);
        }
    }

    /**
     * Метод отключения слушателей мыши.
     */
    public void stopListenToPeriphery() {

        if( _menuMode == MenuMode.MAIN_MENU ) {

            removeMouseListener(_mainMenuMouseListener);

        } else if ( _menuMode == MenuMode.LEVEL_MENU ) {

            removeMouseListener(_levelMenuMouseListener);
        }
    }

    /**
     * Метод подключения слушателей мыши.
     */
    public void startListenToPeriphery() {

        if( _menuMode == MenuMode.MAIN_MENU ) {

            addMouseListener(_mainMenuMouseListener);

        } else if ( _menuMode == MenuMode.LEVEL_MENU ) {

            addMouseListener(_levelMenuMouseListener);
        }
    }

    /**
     * Метод отрисовки главного меню.
     *
     * @param g среда отрисовки.
     */
    private void paintMainMenu(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        g.drawString("Новая игра", _newGameBtn.x + 60,
                     _newGameBtn.y + 50);

        g.drawString("Продолжить", _continueGameBtn.x + 40,
                     _continueGameBtn.y + 50);

        g.drawString("Выбор уровня", _chooseLevelBtn.x + 20,
                     _chooseLevelBtn.y + 50);

        g.drawString("Выход", _quitGameBtn.x + 115,
                     _quitGameBtn.y + 50);

        g2d.draw(_newGameBtn);
        g2d.draw(_continueGameBtn);
        g2d.draw(_chooseLevelBtn);
        g2d.draw(_quitGameBtn);
    }

    /**
     * Метод отрисовки меню выбора уровня.
     *
     * @param g среда отрисовки.
     */
    private void paintLevelMenu(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        /* Шрифт */
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(20F);
        g.setFont(newFont);

        /* Получаем названия уровней */
        ArrayList<String> levelNames = GameModel.getLevelNames();

        int size = levelNames.size();
        int xStart = 65;
        int yStart = 350;

        /* Кнопки уровней */
        for( int i = 0; i < size; ++i ) {

            String lvlName = levelNames.get(i);
            Rectangle tmp = new Rectangle(xStart,
                                          yStart,
                                          LVL_BUTTON_WIDTH,LVL_BUTTON_HEIGHT);

            paintCenteredString(g, tmp,lvlName,yStart,xStart);
            g2d.draw(tmp);
            yStart += LVL_BUTTON_HEIGHT + LVL_BUTTON_SPLITTER;
            if (i > 6 && i %7 == 0){
                xStart = xStart + LVL_BUTTON_WIDTH + 25;
                yStart = 350;
            }
            _levelBtns.add(tmp);
        }

        /* Кнопка назад */
        _backToMainMenuBtn = new Rectangle(xStart,yStart,
                                           LVL_BUTTON_WIDTH,LVL_BUTTON_HEIGHT);
        g2d.draw(_backToMainMenuBtn);
        paintCenteredString(g, _backToMainMenuBtn,"Назад",
                            _backToMainMenuBtn.y,_backToMainMenuBtn.x);
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
     * Метод получения номера уровня по координатам клика мыши.
     *
     * @param x координата X клика мыши.
     * @param y координата Y клика мыши.
     * @return int - номер уровня.
     */
    private int getLevelNumberByClickCoordinates(int x,int y) {

        int size = _levelBtns.size();
        for( int i = 0;i < size;++i ) {

            Rectangle btn = _levelBtns.get(i);

            if ( x >= btn.x && x <= (btn.x + btn.width) ) {

                if ( y >= btn.y && y <= (btn.y + btn.height) ) {

                    return i;
                }
            }
        }
        return -1;
    }

    //////////////////////////// Сигналы //////////////////////////////////////

    /**
     * Метод добавления слушателя меню.
     *
     * @param l слушатель.
     */
    public void addMenuListener(MenuListener l) {

        _listenerList.add(l);
    }

    /**
     * Метод удаления слушателя меню.
     *
     * @param l слушатель.
     */
    public void removeMenuListener(MenuListener l) {

        _listenerList.remove(l);
    }

    /**
     * Метод испускания сигнала о начале новой игры.
     */
    protected void fireStartCareerClicked() {

        MenuEvent e = new MenuEvent(this);
        e.setChoosenLevel(0);
        for ( Object listener : _listenerList ) {

            ((MenuListener) listener).startCareerClicked(e);
        }
    }

    /**
     * Метод испускания сигнала о продолжении игры.
     */
    protected void fireContinueCareerClicked() {

        MenuEvent e = new MenuEvent(this);
        for ( Object listener : _listenerList ) {

            ((MenuListener) listener).continueCareerClicked(e);
        }
    }

    /**
     * Метод испускания сигнала о прохождении конкретного уровня.
     *
     * @param levelNumber номер уровня.
     */
    protected void fireLevelChoosen(int levelNumber) {

        MenuEvent e = new MenuEvent(this);
        e.setChoosenLevel(levelNumber);
        for ( Object listener : _listenerList ) {

            ((MenuListener) listener).levelChoosen(e);
        }
    }

    /** Список слушателей меню */
    private ArrayList _listenerList = new ArrayList();

    //////////////////////////// Слушатели ////////////////////////////////////

    /**
     * Внутренний класс - слушатель мыши для главного меню.
     */
    private class MainMenuClickListener extends MouseAdapter {

        /**
         * Метод, обрабатывающий клики мыши.
         *
         * @param e событие.
         */
        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            /* Новая игра */
            if ( x >= _newGameBtn.x
                 && x <= (_newGameBtn.x + _newGameBtn.width) ) {

                if ( y >= _newGameBtn.y
                     && y <= (_newGameBtn.y + _newGameBtn.height) ) {

                    fireStartCareerClicked();
                }
            }

            /* Продолжить игру */
            if ( x >= _continueGameBtn.x
                 && x <= (_continueGameBtn.x + _continueGameBtn.width) ) {

                if ( y >= _continueGameBtn.y
                     && y <= (_continueGameBtn.y + _continueGameBtn.height) ) {

                    fireContinueCareerClicked();
                }
            }

            /* Выбрать уровень */
            if ( x >= _chooseLevelBtn.x
                 && x <= (_chooseLevelBtn.x + _chooseLevelBtn.width) ) {

                if ( y >= _chooseLevelBtn.y
                     && y <= (_chooseLevelBtn.y + _chooseLevelBtn.height) ) {

                    stopListenToPeriphery();
                    _menuMode = MenuMode.LEVEL_MENU;
                    startListenToPeriphery();
                }
            }

            /* Выйти из игры */
            if ( x >= _quitGameBtn.x
                 && x <= (_quitGameBtn.x + _quitGameBtn.width) ) {

                if ( y >= _quitGameBtn.y
                     && y <= (_quitGameBtn.y + _quitGameBtn.height) ) {

                    GameModel.saveGameProgress();
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Внутренний класс - слушатель мыши для меню выбора уровня.
     */
    private class LevelMenuClickListener extends MouseAdapter {

        /**
         * Метод, обрабатывающий клики мыши.
         *
         * @param e событие.
         */
        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            if ( x >= _backToMainMenuBtn.x
                 && x <= (_backToMainMenuBtn.x + _backToMainMenuBtn.width) ) {

                if ( y >= _backToMainMenuBtn.y
                     && y <= (_backToMainMenuBtn.y + _backToMainMenuBtn.height) ) {

                    stopListenToPeriphery();
                    _menuMode = MenuMode.MAIN_MENU;
                    startListenToPeriphery();
                }
            }

            /* Если не перешли в главное меню */
            if( _menuMode == MenuMode.LEVEL_MENU ){

                int levelNumber = getLevelNumberByClickCoordinates(x, y);

                if( levelNumber != -1 ) {

                    stopListenToPeriphery();
                    _menuMode = MenuMode.MAIN_MENU;
                    startListenToPeriphery();
                    fireLevelChoosen(levelNumber);
                }
            }
        }
    }

    //////////////////////////// Константы ////////////////////////////////////

    /** Путь к фону меню */
    private static final String MENU_BACKGROUND_PATH = "/badmagic/resources/menu.png";

    /** Путь к шрифту */
    private static final String MENU_FONT_PATH = "/badmagic/resources/menu.ttf";

    /** Размер шрифта */
    private static final float FONT_SIZE = 50;

    /** Тип шрифта */
    private static final String FONT_TYPE = "Comic Sans MS";

    /** Цвет шрифта */
    private static final Color FONT_COLOR = new Color(205, 133, 63);

    /** Координата X приветствующей строки */
    private static final int WELCOME_STRING_X = 350;

    /** Координата Y приветствующей строки */
    private static final int WELCOME_STRING_Y = 100;

    /** Приветствующая строка */
    private static final String WELCOME_STRING = "Welcome To Bad Magic";

    /** Строка меню выбора уровня */
    private static final String LEVEL_STRING = "Выберите уровень";

    /** Цвет фона */
    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);

    /** Ширина кнопок уровней */
    private static final int LVL_BUTTON_WIDTH = 300;

    /** Высота кнопок уровней */
    private static final int LVL_BUTTON_HEIGHT = 30;

    /** Разделитель между кнопками уровней */
    private static final int LVL_BUTTON_SPLITTER = 10;

    /** Ширина кнопок меню */
    private static final int BUTTON_WIDTH = 400;

    /** Высота кнопок меню */
    private static final int BUTTON_HEIGHT = 60;

    /** "Кнопка" Новая игра */
    private Rectangle _newGameBtn
                      = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                                      300, BUTTON_WIDTH, BUTTON_HEIGHT);

    /** "Кнопка" Продолжить */
    private Rectangle _continueGameBtn
                      = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                                      400, BUTTON_WIDTH, BUTTON_HEIGHT);

    /** "Кнопка" Выбрать уровень */
    private Rectangle _chooseLevelBtn
                      = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                                      500, BUTTON_WIDTH, BUTTON_HEIGHT);

    /** "Кнопка" Выход*/
    private Rectangle _quitGameBtn
                      = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                                      600, BUTTON_WIDTH, BUTTON_HEIGHT);


    //////////////////////////// Данные ///////////////////////////////////////

    /** Режим меню */
    private enum MenuMode {

        MAIN_MENU,
        LEVEL_MENU
    }

    /** "Кнопка" Назад */
    private Rectangle _backToMainMenuBtn;

    /** Текущий режим меню */
    private MenuMode _menuMode = MenuMode.MAIN_MENU;

    /** Список "кнопок" уровней */
    ArrayList<Rectangle> _levelBtns = new ArrayList<>();

    /** Слушатель мыши для главного меню */
    private MainMenuClickListener _mainMenuMouseListener
                                                = new MainMenuClickListener();

    /** Слушатель мыши для меню выбора уровня */
    private LevelMenuClickListener _levelMenuMouseListener
                                                = new LevelMenuClickListener();
}
