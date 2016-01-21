/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package badmagic.model.gameobjects;

import badmagic.model.GameField;

/**
 *
 * @author 1
 */
class MagicLock extends Lock{

    public MagicLock(GameField field) {
        super(field);
    }

    /**
     * Метод, закрывающий замок
     * @param key - ключ
     * @return Флаг успеха действия
     */
    @Override
    public boolean lock (GameObject key){
         if (key instanceof Spell){
             _key = key;
             return lock();
         }

         return false;
    }

    /**
     * Метод для открытия объекта (снятия с него замка)
     * @param key - ключ-заклинанеие
     * @return Флаг - открылся ли объект
     */
    @Override
    public boolean unlock(GameObject key){
        /** Проверяем, не пытаемся ли открыть открытое */
        if (!this.isLocked()){
            return true;
        }
        
        /** Если переданный ключ является заклинанием и идентификаторы совпали */
       if (key instanceof Spell && ((Spell)_key).equals((Spell)_key)){
           _lockState = false;
       }
      
       return !isLocked();
    }
    
}
