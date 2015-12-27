package badmagic;
import badmagic.events.MenuEvent;
import badmagic.events.MenuListener;
import badmagic.events.PanelListener;
import badmagic.model.GameModel;
import badmagic.view.GameMenu;
import badmagic.view.GamePanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

        try {

            _gameModel = new GameModel();

        } catch ( Exception ex ) {

            ex.printStackTrace();
            showErrorMessage(ex.toString());
            System.exit(0);
        }

        _gamePanel = new GamePanel(_gameModel);
        _gameMenu = new GameMenu();
        _gameMenu.startListenToPeriphery();
        _gameMenu.addMenuListener(new MenuObserver());
        _gamePanel.addPanelListener(new PanelObserver());

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

    private void showErrorMessage(String msg) {

        JOptionPane.showMessageDialog(  _window,
                                        msg,
                                        "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
    }

    private void changePanelTo(JPanel panel) {

        _window.getContentPane().removeAll();
        _window.getContentPane().add(panel);
        _window.revalidate();
        _window.repaint();
        panel.requestFocus();
    }

    private class MenuObserver implements MenuListener {

        @Override
        public void startCareerClicked(MenuEvent e) {

            log.info("Пойман сигнал из класса GameMenu о начале Новой игры");
            _gameMenu.stopListenToPeriphery();
            changePanelTo(_gamePanel);
            _gamePanel.startListenToPeriphery();

            try {

                _gamePanel.startNewCareer();

            } catch ( Exception ex ) {

                showErrorMessage(ex.toString());
            }
        }

        @Override
        public void continueCareerClicked(MenuEvent e) {

            log.info("Пойман сигнал из класса GameMenu о Продолжении игры");
            _gameMenu.stopListenToPeriphery();
            changePanelTo(_gamePanel);
            _gamePanel.startListenToPeriphery();

            try {

                _gamePanel.continueCareer();

            } catch ( Exception ex ) {

                showErrorMessage(ex.toString());
            }
        }

        @Override
        public void levelChoosen(MenuEvent e) {

            log.info("Пойман сигнал из класса GameMenu об Игре конкретного уровня");
            _gameMenu.stopListenToPeriphery();
            changePanelTo(_gamePanel);
            _gamePanel.startListenToPeriphery();

            try {

                _gamePanel.oneLevelMode(e.getChoosenLevel());

            } catch ( Exception ex ) {

                showErrorMessage(ex.toString());
            }
        }
    }

    private class PanelObserver implements PanelListener {

        @Override
        public void mainMenuClicked(EventObject e) {

            _gamePanel.stopListenToPeriphery();
            _gameMenu.startListenToPeriphery();
            _window.getContentPane().removeAll();
            _window.setContentPane(_gameMenu);
            _window.revalidate();
            _window.repaint();
            _gameMenu.requestFocus();
        }
    }

    private static final String TITLE = "Bad Magic";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private JFrame _window;
    private GameModel _gameModel;
    private GamePanel _gamePanel;
    private GameMenu _gameMenu;
}
