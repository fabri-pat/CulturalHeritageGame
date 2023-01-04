package it.uniba.sms222325.esplicative;

import com.badlogic.gdx.InputAdapter;

public class Processor extends InputAdapter {

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Tocco nella posizione " + screenX + "," + screenY);
        System.out.println("è stato usato il dito " + pointer + " e il tasto " + button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }
}
