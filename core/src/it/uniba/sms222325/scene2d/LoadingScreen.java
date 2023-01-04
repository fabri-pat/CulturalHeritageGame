package it.uniba.sms222325.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import it.uniba.sms222325.BaseScreen;
import it.uniba.sms222325.MyGdxGame;

public class LoadingScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;      // le skin sono set di elementi dell'interfaccia
    private Label loading;

    public LoadingScreen(final MyGdxGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        loading = new Label("Loading...", skin);
        loading.setPosition(320 - loading.getWidth() / 2, 100 - loading.getHeight() / 2);
        stage.addActor(loading);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);		// tinge lo schermo di un colore
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		// pulisce la scheda video da immagini precedenti

        if (game.getManager().update()) {       // se finisce di caricare i media chiama finishLoading(), altrimenti stampa a schermo il progresso di caricamento
            game.finishLoading();
        } else {
            int progress = (int) (game.getManager().getProgress() * 100);
            loading.setText("Loading... " + progress + "%");
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
