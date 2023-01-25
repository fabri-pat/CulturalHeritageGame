package it.uniba.sms222325.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextEntity extends Actor {

    private BitmapFont bitmapFontName;
    private String text;
    private int x, y;

    public TextEntity(String text, int x, int y){
        this.text = text;
        bitmapFontName = new BitmapFont();
        this.x = x;
        this.y = y;
    }

    public void updateText(String text){
        this.text = text;
    }

    public void updatePosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        bitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFontName.draw(batch, text, this.x, this.y);
    }

}
