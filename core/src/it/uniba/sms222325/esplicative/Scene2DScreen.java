package it.uniba.sms222325.esplicative;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import it.uniba.sms222325.BaseScreen;
import it.uniba.sms222325.MyGdxGame;

public class Scene2DScreen extends BaseScreen {

    public Scene2DScreen(MyGdxGame game) {
        super(game);
        texturePlayer = new Texture("badlogic.jpg");        // specifico centralmente qua la texture e non nel costruttore di PlayerActor
        textureBlock = new Texture("block.png");
    }

    private Stage stage;
    private PlayerActor player;
    private BlockActor block;
    private Texture texturePlayer;
    private Texture textureBlock;

    @Override
    public void show() {
        stage = new Stage();
        stage.setDebugAll(true);        // per le collisioni
        player = new PlayerActor(texturePlayer);
        block = new BlockActor(textureBlock);
        player.setPosition(20, 100);
        block.setPosition(1500, 100);
        stage.addActor(player);
        stage.addActor(block);
    }

    @Override
    public void hide() {
        stage.dispose();
        texturePlayer.dispose();
        textureBlock.dispose();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1, 1);		// tinge lo schermo di un colore??
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		// pulisce la scheda video da immagini precedenti
        stage.act();
        checkCollision();
        stage.draw();
    }

    private void checkCollision() {
        if (player.isAlive() && (player.getX() + player.getWidth() > block.getX())) {
            System.out.println("Collisione!");
            player.setAlive(false);
        }
    }
}
