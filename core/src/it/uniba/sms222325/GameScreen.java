package it.uniba.sms222325;

import static it.uniba.sms222325.Constants.PIXELS_IN_METER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import it.uniba.sms222325.entities.BlockEntity;
import it.uniba.sms222325.entities.PlayerEntity;
import it.uniba.sms222325.entities.SoilEntity;
import it.uniba.sms222325.entities.TextEntity;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {

    private Stage stage;        // Scene2D usa la classe Stage
    private World world;        // Box2D usa la classe World
    private PlayerEntity player;        // attore protagonista (Scene2D lavora con gli attori)
    private List<SoilEntity> soilList = new ArrayList<SoilEntity>();
    private List<BlockEntity> blockList = new ArrayList<BlockEntity>();
    private Sound jumpSound, dieSound;
    private Music bgMusic;
    private int score;
    private TextEntity scoreText;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private SpriteBatch batchBackground;        // serve per il draw dello sfondo
    private final Prefs prefs;      // classe che permette di gestire le informazioni salvabili in locale
    private int speedPoint = 1000;      // punto di aummento di velocità

    public GameScreen(final MyGdxGame game) {
        super(game);

        prefs = new Prefs();

        // Carico i media di tipo audio
        jumpSound = game.getManager().get("audio/jump.ogg");
        dieSound = game.getManager().get("audio/die.ogg");
        bgMusic = game.getManager().get("audio/song.ogg");

        stage = new Stage(new FitViewport(640, 360));           // imposto la finestra a 640*360px
        world = new World(new Vector2(0, -9), true);       // creo un ambiente in cui ho -9 di gravità (quasi a quella terrestre che è -9.8)

        batchBackground = new SpriteBatch();

        world.setContactListener(new ContactListener() {

            // funzione di utilità, verifica che due attori siano in collisione
            private boolean areCollided(Contact contact, Object userA, Object userB) {
                return (contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB)) || (contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA));
            }

            // funzione chiamata quando avviene una collisione
            @Override
            public void beginContact(Contact contact) {

                // collisione tra protagonista e terreno
                if (areCollided(contact, "player", "soil")) {
                    player.setOnFloor(true);
                }

                // collisione tra protagonista e blocco
                if (areCollided(contact, "player", "block")) {
                    player.setAlive(false);
                    if (score > prefs.getHighScore()) {
                        prefs.setHighScore(score);
                    }
                    speedPoint = 1000;      // reset
                    bgMusic.stop();
                    dieSound.play();

                    stage.addAction(Actions.sequence(
                            Actions.delay(1.5f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    game.setScreen(game.gameOverScreen);        // mostra la schermata di game over
                                }
                            })
                    ));
                }
            }

            @Override
            public void endContact(Contact contact) {
                if (areCollided(contact, "player", "soil")) {
                    player.setOnFloor(false);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void show() {
        // evito che venga istanziata ogni volta una nuova Texture
        Texture playerTexture = game.getManager().get("player.png");
        Texture soilTexture = game.getManager().get("soil.png");
        Texture blockTexture = game.getManager().get("block.png");

        int city = (int) (Math.random() * (5 - 1 + 1) + 1);     // (Math.random() * (max - min + 1) + min) dove il valore max è escluso, min è incluso
        switch (city) {
            case 1:
                backgroundTexture = game.getManager().get("cities/Firenze1.png");
                break;
            case 2:
                backgroundTexture = game.getManager().get("cities/Pompei1.png");
                break;
            case 3:
                backgroundTexture = game.getManager().get("cities/Segesta1.png");
                break;
            case 4:
                backgroundTexture = game.getManager().get("cities/Roma1.png");
                break;
            default:
                backgroundTexture = game.getManager().get("cities/Roma1.png");
        }
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f, 1.5f));     // se passo 0.5f, il giocatore viene renderizzato al livello del terreno
        soilList.add(new SoilEntity(world, soilTexture, 0, 1000, 1));

        for (int i = 7; i < 999; i = i + 6) {       // generazione casuale dei blocchi
            int randomBlockPosition = (int) (Math.random() * (5 - 3 + 1) + 3);
            randomBlockPosition = randomBlockPosition + i;
            blockList.add(new BlockEntity(world, blockTexture, randomBlockPosition, 1));
        }
        //soilList.add(new SoilEntity(world, soilTexture, 13, 10, 2));
        //blockList.add(new BlockEntity(world, blockTexture, 7, 1));      // al valore y s'intende il livello dello strato di terreno sopra il quale verrà poszionato il blocco
        //blockList.add(new BlockEntity(world, blockTexture, 17, 2));
        //blockList.add(new BlockEntity(world, blockTexture, 26, 1));
        //blockList.add(new BlockEntity(world, blockTexture, 60, 1));

        for (SoilEntity soil : soilList) {
            stage.addActor(soil);
        }
        for (BlockEntity block : blockList) {
            stage.addActor(block);
        }

        scoreText = new TextEntity("SCORE: " + score, 0, 0);

        stage.addActor(player);
        stage.addActor(scoreText);

        bgMusic.setVolume(0.75f);
        bgMusic.play();
    }

    public void hide() {
        bgMusic.stop();

        player.detach();
        player.remove();
        for (SoilEntity soil : soilList) {
            soil.detach();
            soil.remove();
        }
        for (BlockEntity block : blockList) {
            block.detach();
            block.remove();
        }

        soilList.clear();
        blockList.clear();

        scoreText.remove();
    }

    // funzione che renderizza tot frames al secondo
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);		// tinge lo schermo di un colore
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		// pulisce la scheda video da immagini precedenti

        if (Gdx.input.isTouched()  &&  player.isOnFloor()) {        // per far saltare il protagonista, tenendo premuto lo schermo
            jumpSound.play();
            player.jump();
        }

        if (player.getX() == 0 && player.isAlive()) {
            stage.getCamera().position.set(320, 180, 0);        // reset della visuale nella posizione iniziale
            stage.getCamera().update();
        }

        if (player.getX() > 150 && player.isAlive()) {
            stage.getCamera().translate(player.getActualSpeed() * delta * PIXELS_IN_METER, 0, 0);      // sposto la visuale con l'avanzare del giocatore
        }

        if (player.isAlive()) {
            score = (int) player.getX();
            scoreText.updateText("SCORE: " + score);
            scoreText.updatePosition((int) stage.getCamera().position.x + 180, 20);
        }

        if (player.getX() > speedPoint && player.isAlive()) {
            player.moreSpeed();
            speedPoint = speedPoint + 999;
        }

        batchBackground.begin();
        backgroundSprite.draw(batchBackground);
        batchBackground.end();

        stage.act();        // prima di introdurre le forze in campo
        world.step(delta, 6, 2);        // applicazione delle forze
        stage.draw();       // disegna con i parametri calcolati
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        batchBackground.dispose();

        stage.dispose();
        world.dispose();
    }

}
