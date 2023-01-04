package it.uniba.sms222325.esplicative;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    private Texture player;
    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public PlayerActor(Texture player) {
        this.player = player;
        this.alive = true;
        setSize(player.getWidth(), player.getHeight());     // setta i bordi per le collisioni
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(player, getX(), getY());
    }
}
