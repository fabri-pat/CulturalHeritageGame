package it.uniba.sms222325;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Hud {
    private Stage stage;
    private ImageButton imageButton;
    private int hudClicked = 0;

    public Hud(Texture buttonTexture) {
        stage = new Stage(new FitViewport(640, 360)); //create stage with the stageViewport and the SpriteBatch given in Constructor

        Table table = new Table();
        table.setPosition(0, 360);
        //add the Buttons etc.

        Texture texture = buttonTexture;
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        imageButton = new ImageButton(drawable);
        //imageButton.setPosition(200, 200);
        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                hudClicked = 1; //pausa
            }
        });

        table.add(imageButton).expand().top().left();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
    }

    public Stage getStage() { return stage; }

    public void dispose(){
        stage.dispose();
    }

    public int getHudClicked() {
        return hudClicked;
    }

    public void resetHudValue() {
        hudClicked = 0;
    }
}
