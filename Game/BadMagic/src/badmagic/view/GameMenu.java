package badmagic.view;

import badmagic.BadMagic;
import badmagic.events.MenuListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameMenu extends JPanel   {
    
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
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, 
                               RenderingHints.VALUE_ANTIALIAS_ON );
        setBackground(BACKGROUND_COLOR);
        Font font = new Font(FONT_TYPE,Font.BOLD,FONT_SIZE);
        g.setFont(font);
        g.setColor(FONT_COLOR);
        g.drawString(WELCOME_STRING, WELCOME_STRING_X, WELCOME_STRING_Y);
        
        font = new Font(FONT_TYPE,Font.BOLD,FONT_SIZE - 20);
        g.setFont(font);
        
        g.drawString("Новая игра",  newGameBtn.x + 65,
                                    newGameBtn.y + 35);
        
        g.drawString("Продолжить игру", continueGameBtn.x + 20,
                                        continueGameBtn.y + 35);
        
        g.drawString("Выбрать уровень", chooseLevelBtn.x + 25,
                                        chooseLevelBtn.y + 35);
        
        g.drawString("Выход",   quitGameBtn.x + 100,
                                quitGameBtn.y + 35);
        
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
    
    protected void  fireStartCareerClicked() {
        
        EventObject e = new EventObject(this);
        for( Object listener : _listenerList ) {
            
            ((MenuListener)listener).startCareerClicked(e);
        }
    }
    
    protected void  fireContinueCareerClicked() {
        
        EventObject e = new EventObject(this);
        for( Object listener : _listenerList ) {
            
            ((MenuListener)listener).continueCareerClicked(e);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    private class ClickListener extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            int x = e.getX();
            int y = e.getY();
            
            /* Новая игра */
            if( x >= newGameBtn.x && 
                x <= (newGameBtn.x + newGameBtn.width) ) {
                
                if( y >= newGameBtn.y && 
                    y <= (newGameBtn.y + newGameBtn.height) ){
                    
                    BadMagic.log.info("Нажата кнопка \"Новая игра\"");
                    fireStartCareerClicked();
                }
            }
            
            /* Продолжить игру */
            if( x >= continueGameBtn.x && 
                x <= (continueGameBtn.x + continueGameBtn.width) ) {
                
                if( y >= continueGameBtn.y && 
                    y <= (continueGameBtn.y + continueGameBtn.height) ){
                    
                    BadMagic.log.info("Нажата кнопка \"Продолжить игру\"");
                    fireContinueCareerClicked();
                }
            }
            
            /* Выбрать уровень */
            if( x >= chooseLevelBtn.x && 
                x <= (chooseLevelBtn.x + chooseLevelBtn.width) ) {
                
                if( y >= chooseLevelBtn.y && 
                    y <= (chooseLevelBtn.y + chooseLevelBtn.height) ){
                    
                    BadMagic.log.info("Нажата кнопка \"Выбрать уровень\"");
                }
            }
            
            /* Выйти из игры */
            if( x >= quitGameBtn.x && 
                x <= (quitGameBtn.x + quitGameBtn.width) ) {
                
                if( y >= quitGameBtn.y && 
                    y <= (quitGameBtn.y + quitGameBtn.height) ){
                    
                    BadMagic.log.info("Нажата кнопка \"Выход\"");
                    System.exit(0);
                }
            }
        }
    }
    
    private static final int FONT_SIZE = 50;
    private static final String FONT_TYPE = "Comic Sans MS";
    private static final Color FONT_COLOR = new Color(205, 133, 63);
    
    private static final int WELCOME_STRING_X = 350;
    private static final int WELCOME_STRING_Y = 100;
    private static final String WELCOME_STRING = "Welcome To Bad Magic";
    
    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);
    
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 50;
    
    private Rectangle newGameBtn = 
                            new Rectangle(  BadMagic.getWindowWidth()/2 - 150,
                                            200,BUTTON_WIDTH,BUTTON_HEIGHT);
    
    private Rectangle continueGameBtn = 
                            new Rectangle(  BadMagic.getWindowWidth()/2 - 150,
                                            300,BUTTON_WIDTH,BUTTON_HEIGHT);
    
    private Rectangle chooseLevelBtn = 
                            new Rectangle(  BadMagic.getWindowWidth()/2 - 150,
                                            400,BUTTON_WIDTH,BUTTON_HEIGHT);
    
    private Rectangle quitGameBtn = 
                            new Rectangle(  BadMagic.getWindowWidth()/2 - 150,
                                            500,BUTTON_WIDTH,BUTTON_HEIGHT);
}
