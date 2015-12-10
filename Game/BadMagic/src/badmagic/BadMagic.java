package badmagic;
import badmagic.model.GameModel;
import badmagic.view.GameMenu;
import badmagic.view.GamePanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class BadMagic  {

    public static final Logger log = Logger.getLogger(BadMagic.class.getName());
    
    public static void main(String[] args) {
     
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new BadMagic();
            }
        });
    }
    
    
    private BadMagic() {
         
        _gameModel = new GameModel();
        _gamePanel = new GamePanel(_gameModel);
        _gameMenu = new GameMenu();
        
        _window = new JFrame(TITLE); 
        _window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _window.setSize(WIDTH, HEIGHT);
        _window.setResizable(false);
        _window.setLocationRelativeTo(null);
        _window.setVisible(true);
        _window.setContentPane(_gameMenu);
        _window.pack();
    }
    
    
    public static int getWindowWidth() {
        
        return WIDTH;
    }
    
    public static int getWindowHeight() {
        
        return HEIGHT;
    }
    
    private static final String TITLE = "Bad Magic";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private JFrame _window;
    private GameModel _gameModel;
    private GamePanel _gamePanel;
    private GameMenu _gameMenu;
}
