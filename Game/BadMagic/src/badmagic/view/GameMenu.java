package badmagic.view;

import badmagic.BadMagic;
import badmagic.events.MenuListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameMenu extends JPanel {

    public GameMenu() {

        super();
        addMouseListener(new ClickListener());
        setPreferredSize(new Dimension( BadMagic.getWindowWidth(),
                                        BadMagic.getWindowHeight()));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
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
            
        } catch (IOException e) {
            
            e.printStackTrace();
            BadMagic.log.info("Cannot load recources");
            
        } catch (FontFormatException e) {
            
            e.printStackTrace();
            BadMagic.log.info("Cannot register font"); 
        }

        //Установка изображения
        g.drawImage(menu_background.getScaledInstance(BadMagic.getWindowWidth(), BadMagic.getWindowHeight(), Image.SCALE_DEFAULT), 0, 0, this);
        //Установка шрифта            
        g.setFont(menu_font);
        g.setColor(FONT_COLOR);
        g.drawString(WELCOME_STRING, WELCOME_STRING_X, WELCOME_STRING_Y);

        g.drawString("Новая игра", newGameBtn.x + 60,
                newGameBtn.y + 50);

        g.drawString("Продолжить", continueGameBtn.x + 40,
                continueGameBtn.y + 50);

        g.drawString("Выбор уровня", chooseLevelBtn.x + 20,
                chooseLevelBtn.y + 50);

        g.drawString("Выход", quitGameBtn.x + 115,
                quitGameBtn.y + 50);

        g2d.draw(newGameBtn);
        g2d.draw(continueGameBtn);
        g2d.draw(chooseLevelBtn);
        g2d.draw(quitGameBtn);

    }

    ///////////////////////////////////////////////////////////////////////////
    private ArrayList _listenerList = new ArrayList();

    public void addMenuListener(MenuListener l) {

        _listenerList.add(l);
    }

    public void removeMenuListener(MenuListener l) {

        _listenerList.remove(l);
    }
    
    protected void fireStartCareerClicked() {

        EventObject e = new EventObject(this);
        for (Object listener : _listenerList) {

            ((MenuListener) listener).startCareerClicked(e);
        }
    }

    protected void fireContinueCareerClicked() {

        EventObject e = new EventObject(this);
        for (Object listener : _listenerList) {

            ((MenuListener) listener).continueCareerClicked(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    private class ClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            /* Новая игра */
            if (x >= newGameBtn.x
                    && x <= (newGameBtn.x + newGameBtn.width)) {

                if (y >= newGameBtn.y
                        && y <= (newGameBtn.y + newGameBtn.height)) {

                    BadMagic.log.info("Нажата кнопка \"Новая игра\"");
                    fireStartCareerClicked();
                }
            }

            /* Продолжить игру */
            if (x >= continueGameBtn.x
                    && x <= (continueGameBtn.x + continueGameBtn.width)) {

                if (y >= continueGameBtn.y
                        && y <= (continueGameBtn.y + continueGameBtn.height)) {

                    BadMagic.log.info("Нажата кнопка \"Продолжить игру\"");
                    fireContinueCareerClicked();
                }
            }

            /* Выбрать уровень */
            if (x >= chooseLevelBtn.x
                    && x <= (chooseLevelBtn.x + chooseLevelBtn.width)) {

                if (y >= chooseLevelBtn.y
                        && y <= (chooseLevelBtn.y + chooseLevelBtn.height)) {

                    BadMagic.log.info("Нажата кнопка \"Выбрать уровень\"");
                }
            }

            /* Выйти из игры */
            if (x >= quitGameBtn.x
                    && x <= (quitGameBtn.x + quitGameBtn.width)) {

                if (y >= quitGameBtn.y
                        && y <= (quitGameBtn.y + quitGameBtn.height)) {

                    BadMagic.log.info("Нажата кнопка \"Выход\"");
                    System.exit(0);
                }
            }
        }
    }

    private static final String MENU_BACKGROUND_PATH = "/badmagic/resources/menu.png";
    private static final String MENU_FONT_PATH = "/badmagic/resources/menu.ttf";
    
    private static final float FONT_SIZE = 50;
    private static final String FONT_TYPE = "Comic Sans MS";
    private static final Color FONT_COLOR = new Color(205, 133, 63);

    private static final int WELCOME_STRING_X = 350;
    private static final int WELCOME_STRING_Y = 100;
    private static final String WELCOME_STRING = "Welcome To Bad Magic";

    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);

    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 60;

    private Rectangle newGameBtn
            = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                    300, BUTTON_WIDTH, BUTTON_HEIGHT);

    private Rectangle continueGameBtn
            = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                    400, BUTTON_WIDTH, BUTTON_HEIGHT);

    private Rectangle chooseLevelBtn
            = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                    500, BUTTON_WIDTH, BUTTON_HEIGHT);

    private Rectangle quitGameBtn
            = new Rectangle(BadMagic.getWindowWidth() / 6 - 150,
                    600, BUTTON_WIDTH, BUTTON_HEIGHT);
}
