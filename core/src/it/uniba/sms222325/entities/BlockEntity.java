package it.uniba.sms222325.entities;

import static it.uniba.sms222325.Constants.PIXELS_IN_METER;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BlockEntity extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    public BlockEntity(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(x, y + 0.5f);
        body = world.createBody(def);

        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(blockShape, 1);
        fixture.setUserData("block");
        blockShape.dispose();

        setSize(PIXELS_IN_METER, PIXELS_IN_METER);
        setPosition((x - 0.5f) * PIXELS_IN_METER, y * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public Vector2 getPosition() { return body.getPosition(); }

}
