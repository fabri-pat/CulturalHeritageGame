package it.uniba.sms222325.esplicative;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BlockActor extends Actor {

    private Texture block;

    public BlockActor(Texture block) {
        this.block = block;
        setSize(block.getWidth(), block.getHeight());       // setta i bordi per le collisioni
    }

    public void act(float delta) {
        setX(getX()-delta*250);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(block, getX(), getY());
    }

}
