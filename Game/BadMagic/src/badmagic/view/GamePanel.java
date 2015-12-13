package badmagic.view;

import badmagic.model.GameModel;
import badmagic.BadMagic;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;


public class GamePanel extends JPanel {
    
    public GamePanel(GameModel model) {
        
        super();
        setPreferredSize(new Dimension( BadMagic.getWindowWidth(),
                                        BadMagic.getWindowHeight()));
        setFocusable(true);
        requestFocus();
        
        _model = model;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        paintInfoPanel(g);
        paintField(g);
    }
    
    
    private void paintInfoPanel(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        int width = BadMagic.getWindowWidth();
        int height = BadMagic.getWindowHeight();
        BasicStroke borders = new BasicStroke(OBJECTS_BORDER_WIDTH);
        g2d.setStroke(borders);
        
        
        setBackground(BACKGROUND_COLOR);
        g.setColor(OBJECTS_BORDER_COLOR);
        g.drawRect(-1, -1, INFO_PANEL_WIDTH, height);
        
        Font font = new Font(FONT_TYPE,Font.BOLD,FONT_SIZE);
        g.setFont(font);
        g.setColor(FONT_COLOR);
        g.drawString("Уровень:", 5, 30);
        g.drawString("Осталось ходов:", 5, 60);
        
        g.drawRect(60, 650, 70, 30);
        g.drawString("Выход", 70, 670);
    }
    
    private void paintField(Graphics g) {
        
        
    }
    
    private GameModel _model;
    
    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);
    private static final Color OBJECTS_BORDER_COLOR = new Color(205, 133, 63);
    private static final int OBJECTS_BORDER_WIDTH = 2;
    private static final int INFO_PANEL_WIDTH = 200;
    private static final int FONT_SIZE = 15;
    private static final String FONT_TYPE = "Comic Sans MS";
    private static final Color FONT_COLOR = new Color(205, 133, 63);
}
