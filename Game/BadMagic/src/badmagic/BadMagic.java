package badmagic;

import badmagic.events.MenuEvent;
import badmagic.events.MenuListener;
import badmagic.events.PanelListener;
import badmagic.model.GameModel;
import badmagic.view.GameMenu;
import badmagic.view.GamePanel;
import java.util.EventObject;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Главный класс программы.
 *
 * Содержит метод main. Управляет переходом между меню и панелью
 * и запуском игры.
 *
 * @author Alexander Petrenko, Alexander Lyashenko
 */
public class BadMagic  {

    /** Статический логер программы */
    public static final Logger log = Logger.getLogger(BadMagic.class.getName());

    /**
     * Main-метод, запускающий игру.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new BadMagic();
            }
        });
    }

    /**
     * Конструктор класса.
     *
     * Инициализирует поля. Устанавливает текущую панель.
     * Настраивает размеры и свойства окна.
     */
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

    /**
     * Статический метод получения ширины окна.
     *
     * @return int - ширина окна.
     */
    public static int getWindowWidth() {

        return WIDTH;
    }

    /**
     * Статический метод получения высоты окна.
     *
     * @return int - высота окна.
     */
    public static int getWindowHeight() {

        return HEIGHT;
    }

    /**
     * Метод отображения сообщения об ошибке
     *
     * @param msg сообщение.
     */
    private void showErrorMessage(String msg) {

        JOptionPane.showMessageDialog(  _window,
                                        msg,
                                        "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Метод переключения панели главного окна.
     *
     * Переключает пануль с текущей на panel.
     *
     * @param panel панель, которая будет отображаться в окне.
     */
    private void changePanelTo(JPanel panel) {

        _window.getContentPane().removeAll();
        _window.getContentPane().add(panel);
        _window.revalidate();
        _window.repaint();
        panel.requestFocus();
    }

    /**
     * Внутренний класс - слушатель меню.
     */
    private class MenuObserver implements MenuListener {

        /**
         * Метод, обрабатывающий сигнал о начале новой игры.
         * @param e событие.
         */
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

        /**
         * Метод, обрабатывающий сигнал о продолжении игры.
         * @param e событие.
         */
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

        /**
         * Метод, обрбатывающий сигнал о выборе конкретного уровня.
         *
         * @param e событие меню.
         */
        @Override
        public void levelChoosen(MenuEvent e) {

            log.info("Пойман сигнал из класса GameMenu об Игре конкретного уровня");
            _gameMenu.stopListenToPeriphery();
            changePanelTo(_gamePanel);
            _gamePanel.stopListenToPeriphery();
            _gamePanel.startListenToPeriphery();

            try {

                _gamePanel.oneLevelMode(e.getChoosenLevel());

            } catch ( Exception ex ) {

                showErrorMessage(ex.toString());
            }
        }
    }

    /**
     * Внутренний класс - слушатель панели.
     */
    private class PanelObserver implements PanelListener {

        /**
         * Метод, обрабатывающий сигнал о нажатии кнопки "Главное меню".
         * 
         * @param e событие.
         */
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

    //////////////////////////// Константы ////////////////////////////////////

    /** Заголовок окна */
    private static final String TITLE = "Bad Magic";

    /** Ширина окна */
    private static final int WIDTH = 1280;

    /** Высота окна */
    private static final int HEIGHT = 720;

    //////////////////////////// Данные ///////////////////////////////////////

    /** Главное окна */
    private JFrame _window;

    /** Игровая модель */
    private GameModel _gameModel;

    /** Игровая панель */
    private GamePanel _gamePanel;

    /** Игровое меню */
    private GameMenu _gameMenu;
}
