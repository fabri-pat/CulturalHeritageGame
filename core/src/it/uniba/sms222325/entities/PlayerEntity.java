package it.uniba.sms222325.entities;

import static it.uniba.sms222325.Constants.DENSITY;
import static it.uniba.sms222325.Constants.IMPULSE_JUMP;
import static it.uniba.sms222325.Constants.PIXELS_IN_METER;
import static it.uniba.sms222325.Constants.PLAYER_SPEED;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerEntity extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean alive = true, onFloor = true;
    private float actualSpeed = PLAYER_SPEED;

    public PlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;        // dynamic body perchè si muove
        body = world.createBody(def);

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(0.5f, 0.5f);       // sono x e y a partire dal centro di massa della forma, quindi poi vengono moltiplicati per 2 per ottenere la forma finale
        fixture = body.createFixture(playerShape, DENSITY);     // fixture = forma che viene renderizzata, collegata al body
        fixture.setUserData("player");
        playerShape.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER, (body.getPosition().y - 0.5f) * PIXELS_IN_METER);      // sottraggo 0.5 perchè draw() che è di Scene2D, non utilizza il centro di massa come per Box2D, ma l'origine è in basso a sinistra
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {

        if (alive) {
            float velocityY = body.getLinearVelocity().y;
            body.setLinearVelocity(actualSpeed, velocityY);
        }

        if (!onFloor) {
            body.applyForceToCenter(0, -IMPULSE_JUMP * 0.50f, true);
        }
    }

    public void jump() {
        if (onFloor && alive) {
            onFloor = false;
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
        }
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isOnFloor() {
        return onFloor;
    }

    public void setOnFloor(boolean jumping) {
        this.onFloor = jumping;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getActualSpeed() { return actualSpeed; }

    public void moreSpeed() { actualSpeed = actualSpeed + 0.2f; }

}
