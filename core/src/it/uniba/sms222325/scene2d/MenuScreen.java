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

public class MenuScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Image logo;
    private TextButton play;

    public MenuScreen(final MyGdxGame game) {
        super(game);

            stage = new Stage(new FitViewport(640, 360));
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
            //logo = new Image(game.getManager().get("logo.png", Texture.class));
            play = new TextButton("Play", skin);

            play.addCaptureListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(game.gameScreen);
                }
            });

            //logo.setPosition(320 - logo.getWidth() / 2, 320 - logo.getHeight());
            play.setSize(200, 100);
            play.setPosition(220, 50);

            stage.addActor(play);
            //stage.addActor(logo);
        }

        @Override
        public void show() {
            Gdx.input.setInputProcessor(stage);     // bisogna impostare l'input processor, prima di render()
        }

        @Override
        public void hide() {
            Gdx.input.setInputProcessor(null);      // bisogna rimuovere lo stage come input processor alla rimozione dello schermo
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
