package it.uniba.sms222325.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import it.uniba.sms222325.BaseScreen;
import it.uniba.sms222325.MyGdxGame;

public class PauseScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private TextButton resumeButton;
    private TextButton menuButton;
    private ImageButton audioButton;
    private Label pauseLabel;

    public PauseScreen(final MyGdxGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        resumeButton = new TextButton("Resume", skin);
        menuButton = new TextButton("Menu", skin);
        pauseLabel = new Label("PAUSE", skin);

        Texture audioOnTexture = game.getManager().get("block.png");  //inserire immagine audio
        Texture audioOffTexture = game.getManager().get("player.png");  //inserire immagine no audio
        Drawable drawableOn, drawableOff;
        drawableOn = new TextureRegionDrawable(new TextureRegion(audioOnTexture));
        drawableOff = new TextureRegionDrawable(new TextureRegion(audioOffTexture));
        audioButton = new ImageButton(drawableOff, null, drawableOn);
        if(game.gameScreen.prefs.hasSound()) {
            audioButton.setChecked(true);
        } else {
            audioButton.setChecked(false);
        }



        resumeButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);
            }
        });


        menuButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // chiudi il gioco e vai al menu

                //dispose(); rimuove solo gli oggetti dalla memoria, non torna indietro
                //Gdx.app.exit();  //Non va
                //System.exit(-1);  //Questo chiude tutta l'applicazione
                //game.endGame(); // Gdx.app.exit() chiamato in classe MyGdxGame
            }
        });


        audioButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean hasSound = game.gameScreen.prefs.hasSound();
                if(hasSound) {
                    game.gameScreen.prefs.setHasSound(false);
                    audioButton.setChecked(false);
                } else {
                    game.gameScreen.prefs.setHasSound(true);
                    audioButton.setChecked(true);
                }
            }
        });


        resumeButton.setSize(200, 60);
        resumeButton.setPosition(350, 160);
        menuButton.setSize(200, 60);
        menuButton.setPosition(350, 230);
        pauseLabel.setPosition(150, 150);
        audioButton.setSize(50, 50);
        audioButton.setPosition(350, 90);
        stage.addActor(resumeButton);
        stage.addActor(menuButton);
        stage.addActor(audioButton);
        stage.addActor(pauseLabel);
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
