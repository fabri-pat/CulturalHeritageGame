package it.uniba.sms222325.esplicative;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import it.uniba.sms222325.BaseScreen;
import it.uniba.sms222325.MyGdxGame;

public class Box2DScreen extends BaseScreen {

    public Box2DScreen(MyGdxGame game) {
        super(game);
    }

    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;
    private Body playerBody, soilBody, blockBody;
    private Fixture playerFixture, soilFixture, blockFixture;
    private boolean mustJump, playerJumping, playerAlive = true;

    @Override
    public void show() {
        world = new World(new Vector2(0, -10), true);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);        // x = 640 * 2 (altezza - y) / 360 oppure 2 * 16/9
        camera.translate(0, 1);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("soil")) || (fixtureA.getUserData().equals("soil") && fixtureB.getUserData().equals("player"))) {
                    playerJumping = false;     // non posso mettere nel ContactListener il metodo jump() perchè si richiede che l'engine non stia lavorando e invece durante le collisioni, questo avviene
                }
                if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("block")) || (fixtureA.getUserData().equals("block") && fixtureB.getUserData().equals("player"))) {
                    playerAlive = false;
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        playerBody = world.createBody(createPlayerBodyDef());
        soilBody = world.createBody(createSoilBodyDef());
        blockBody = world.createBody(createBlockBodyDef(6f));        // 0.5f per provare la fisica

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(0.5f, 0.5f);       // sono x e y a partire dal centro di massa della forma, quindi vengono moltiplicati per 2 per ottenere la forma finale
        playerFixture = playerBody.createFixture(playerShape, 1);
        playerShape.dispose();
        PolygonShape soilShape = new PolygonShape();
        soilShape.setAsBox(500, 1);       // sono x e y a partire dal centro di massa della forma, quindi vengono moltiplicati per 2 per ottenere la forma finale
        soilFixture = soilBody.createFixture(playerShape, 1);
        soilShape.dispose();
        blockFixture = createBlockFixture(blockBody);

        playerFixture.setUserData("player");
        soilFixture.setUserData("soil");
        blockFixture.setUserData("block");
    }

    private BodyDef createPlayerBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0, 0.5f);
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private BodyDef createSoilBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0, -1);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }

    private BodyDef createBlockBodyDef(float x) {
        BodyDef def = new BodyDef();
        def.position.set(x, 0.5f);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }

    private Fixture createBlockFixture(Body blockBody) {
        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(0.5f, 0.5f);       // sono x e y a partire dal centro di massa della forma, quindi vengono moltiplicati per 2 per ottenere la forma finale
        blockFixture = blockBody.createFixture(blockShape, 1);
        blockShape.dispose();
        return blockFixture;
    }

    @Override
    public void dispose() {
        playerBody.destroyFixture(playerFixture);
        soilBody.destroyFixture(soilFixture);
        blockBody.destroyFixture(blockFixture);
        world.destroyBody(playerBody);
        world.destroyBody(soilBody);
        world.destroyBody(blockBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {       // i metodi render() e update() vengono chiamati 60 volte al secondo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		// pulisce la scheda video da immagini precedenti

        if (mustJump) {
            mustJump = false;
            jump();
        }

        if (Gdx.input.justTouched() && !playerJumping) {
            mustJump = true;
        }

        if (playerAlive) {
            float velocityY = playerBody.getLinearVelocity().y;
            playerBody.setLinearVelocity(1, velocityY);
        }

        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
    }

    private void jump() {
        Vector2 position = playerBody.getPosition();        // non istanzio (new Vector2()) perchè così ho solo il riferimento e inoltre non sovraccarico il garbage collector
        playerBody.applyLinearImpulse(0, 6, position.x, position.y, true);
    }
}
