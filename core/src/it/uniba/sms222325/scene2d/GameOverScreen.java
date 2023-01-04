package it.uniba.sms222325.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import it.uniba.sms222325.BaseScreen;
import it.uniba.sms222325.MyGdxGame;

public class GameOverScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Image gameover;
    private TextButton retry, menu;

    public GameOverScreen(final MyGdxGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        //gameover = new Image(game.getManager().get("gameover.png", Texture.class));
        retry = new TextButton("Retry", skin);
        menu = new TextButton("Menu", skin);

        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);
            }
        });

        menu.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.menuScreen);
            }
        });

        //gameover.setPosition(320 - gameover.getWidth() / 2, 320 - gameover.getHeight());
        retry.setSize(200, 100);
        menu.setSize(200, 80);
        retry.setPosition(60, 50);
        menu.setPosition(380, 50);
        stage.addActor(retry);
        stage.addActor(menu);
        //stage.addActor(gameover);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);		// tinge lo schermo di un colore
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		// pulisce la scheda video da immagini precedenti
        stage.act();
        stage.draw();
    }

}
