package badmagic.view;

import badmagic.model.GameModel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;


public class GamePanel extends JPanel {
    
    public GamePanel(GameModel model) {
        
        super();
        _model = model;
    }
    
    private GameModel _model;
}
