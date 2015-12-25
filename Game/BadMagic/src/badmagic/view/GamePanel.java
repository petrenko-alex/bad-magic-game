package badmagic.view;

import badmagic.model.GameModel;
import badmagic.BadMagic;
import badmagic.events.GameObjectListener;
import badmagic.events.ModelListener;
import badmagic.model.gameobjects.GameObject;
import badmagic.navigation.Direction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import static sun.applet.AppletResourceLoader.getImage;

public class GamePanel extends JPanel {

    public GamePanel(GameModel model) {

        super();
        setPreferredSize(new Dimension(BadMagic.getWindowWidth(),
                BadMagic.getWindowHeight()));
        setFocusable(true);
        requestFocus();

        _model = model;
        _model.addModelListener(new ModelObserver());
        loadPic();
        addKeyListener(new KeyHandler());
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        paintInfoPanel(g);
        paintGrid(g);
        paintObjects(g);
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

        Font font = new Font(FONT_TYPE, Font.BOLD, FONT_SIZE);
        g.setFont(font);
        g.setColor(FONT_COLOR);
        g.drawString("Уровень:", 5, 30);
        g.drawString("Осталось ходов:", 5, 60);

        g.drawRect(60, 650, 70, 30);
        g.drawString("Выход", 70, 670);
    }

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
        g.drawRect(_fieldStartX,_fieldStartY,width,height);

        /* Текстуры */
        for(int i = 1; i < (rows + 1);i++) {

            int y = _fieldStartY + CELL_SIZE * (i-1);
            for(int j = 1; j < (columns + 1);j++) {

                int x = _fieldStartX + CELL_SIZE * (j - 1);
                g.drawImage(_cellPic, x, y, null);
            }
        }

        /* Сетка */
        /* Вертикальные линии */
        for(int i = 1; i <= (columns + 1); i++)
        {
            int x1 = _fieldStartX + CELL_SIZE * (i - 1);
            g.drawLine(x1, _fieldStartY, x1, (_fieldStartY + height));
        }

        /* Горизонтальные линии */
        for(int i = 1; i <= (rows + 1); i++)
        {
            int y1 = _fieldStartY + CELL_SIZE * (i - 1);
            g.drawLine(_fieldStartX, y1, (_fieldStartX + width), y1);
        }
    }

    private void paintObjects(Graphics g) {

        for(GameObject obj : _model.getField().getObjects()) {

            Point objPos = obj.getPosition();
            objPos = getPanelPosition(objPos);
            obj.paint(g, objPos);
        }
    }

    private void loadPic() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        _cellPic = toolkit.getImage(PIC);
    }

    private Point getPanelPosition(Point logPos) {

        int x = _fieldStartX + (CELL_SIZE * (logPos.x - 1));
        int y = _fieldStartY + (CELL_SIZE * (logPos.y - 1));
        return new Point(x,y);
    }

    private class ObjectsObserver implements GameObjectListener {

        @Override
        public void objectMoved(EventObject e) {

            ((GameObject)e.getSource()).paint(getGraphics(), null);
        }
    }

    private class ModelObserver implements ModelListener {

        @Override
        public void levelCompleted(EventObject e) {

            BadMagic.log.info("Элексир достигнут. Уровень пройден.");
        }

        @Override
        public void levelFailed(EventObject e) {

            BadMagic.log.info("Закончились ходы. Уровень провален.");
        }

    }

    private class KeyHandler extends KeyAdapter {

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

                        BadMagic.log.info("Активировать объект");
                        break;
                    }
                    default: {

                        break;
                    }
                }
            }

        }

    }

    private GameModel _model;

    private static final Color BACKGROUND_COLOR = new Color(47, 79, 79);
    private static final Color OBJECTS_BORDER_COLOR = new Color(205, 133, 63);
    private static final int OBJECTS_BORDER_WIDTH = 2;
    private static final int INFO_PANEL_WIDTH = 200;
    private static final int FONT_SIZE = 15;
    private static final String FONT_TYPE = "Comic Sans MS";
    private static final Color FONT_COLOR = new Color(205, 133, 63);
    private static final String PIC = "src/badmagic/resources/brick.png";

    private static final int CELL_SIZE = 64;

    private int _fieldStartX;
    private int _fieldStartY;
    private Image _cellPic;
}
