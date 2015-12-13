package badmagic.view;

import badmagic.model.GameModel;
import badmagic.BadMagic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
        int width = BadMagic.getWindowWidth();
        int height = BadMagic.getWindowHeight();
        
        setBackground(BACKGROUND_COLOR);
        g.drawRect(0, 0, 50, height);
        
    }
    
    
    private GameModel _model;
    
    private static final Color BACKGROUND_COLOR = new Color(175, 255, 175);
}
