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
    private boolean alive = true;
    private boolean onFloor = true;
    private boolean isDashing = false, dashDone = false;
    private boolean isDodging = false;
    private float actualSpeed = PLAYER_SPEED;

    private float timeLeft = 0;
    private String nextAction = "noAction";
    private String actionSound = "noAction";

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

    public PlayerEntity(World world, Texture texture, Vector2 position, Vector2 velocity) {
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

        body.setLinearVelocity(velocity);
        actualSpeed = velocity.x;
        if(velocity.y != 0) onFloor = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METER, (body.getPosition().y - 0.5f) * PIXELS_IN_METER);      // sottraggo 0.5 perchè draw() che è di Scene2D, non utilizza il centro di massa come per Box2D, ma l'origine è in basso a sinistra
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {

        if(timeLeft > 0) {
            timeLeft -= delta;
            if(timeLeft <= 0) {
                isDashing = false;
                isDodging = false;
            }
        }

        if(timeLeft <= 0 && !nextAction.equals("noAction")) {
            if(nextAction.equals("jump")) {
                jump();
            } else if(nextAction.equals("dodge")) {
                dodge();
            } else if(nextAction.equals("dash")) {
                dash();
            }
        }


        if (alive) {
            float velocityY;
            float velocityX;
            if(isDashing) {
                velocityX = 1.5f * actualSpeed;
                velocityY = 0f;
            }
            else {
                velocityX = actualSpeed;
                velocityY = body.getLinearVelocity().y;
            }
            body.setLinearVelocity(velocityX, velocityY);
        }


        if(onFloor) {
            dashReset();
        } else {
            if(!isDashing) {
                body.applyForceToCenter(0, -IMPULSE_JUMP * 0.50f, true);
            }
        }
    }

    private void dash() {
        if (alive && !dashDone) {
            System.out.println("Now next action: Dash");
            isDashing = true;
            isDodging = false;
            dashDone = true;
            nextAction = "noAction";
            actionSound = "dash";
            timeLeft = 0.5f;
        }
    }

    private void dodge() {
        if (onFloor && alive) {
            System.out.println("Now next action: Dodge");
            isDashing = false;
            isDodging = true;
            nextAction = "noAction";
            actionSound = "dodge";
            timeLeft = 0.5f;
        }
    }

    public void jump() {
        if (onFloor && alive) {
            System.out.println("Now next action: Jump");
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
            nextAction = "noAction";
            actionSound = "jump";
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

    public float getActualSpeed() {
        return actualSpeed;
    }

    public void moreSpeed() {
        actualSpeed = actualSpeed + 0.2f;
    }

    public void setActualSpeed(float actualSpeed) {
        this.actualSpeed = actualSpeed;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getVerticalSpeed() {
        return body.getLinearVelocity().y;
    }

    public void setNextAction(String action) {
        nextAction = action;
    }

    public String getNextAction () {
        return nextAction;
    }

    public void dashReset() {
        this.dashDone = false;
    }

    public String getActionSound () {
        return actionSound;
    }

    public void resetActionSound () {
        actionSound = "noAction";
    }

    public boolean isDashing() {
        return isDashing;
    }
}
