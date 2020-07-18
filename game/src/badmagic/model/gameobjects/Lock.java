/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package badmagic.model.gameobjects;

import badmagic.model.GameField;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author 1
 */
public abstract class Lock extends PassiveObject {

    public Lock(GameField field) {
        super(field);
        _lockState = false;
    }

    @Override
    public void paint(Graphics g, Point pos) {

    }

    @Override
    public void loadPic() {

    }

    /**
     * Абстрактный метод, закрывающий объект на новый ключ
     *
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public abstract boolean lock(GameObject key);

    /**
     * Абстрактный метод, закрывающий объект на старый ключ. При отсутствии
     * старого ключа, дейстивие игнорируется
     *
     * @return Флаг успеха действия
     */
    public boolean lock() {
        if (_key != null) {
            _lockState = true;
        }
        return isLocked();
    }

    /**
     * Абстрактный метод, открывающий объект
     *
     * @param key - ключ
     * @return Флаг успеха действия
     */
    public abstract boolean unlock(GameObject key);

    public boolean isLocked() {
        return _lockState;
    }

    protected GameObject _key;
    protected boolean _lockState;
}
