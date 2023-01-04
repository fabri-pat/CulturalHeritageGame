package it.uniba.sms222325.entities;

import static it.uniba.sms222325.Constants.PIXELS_IN_METER;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SoilEntity extends Actor {

    private Texture texture;
    private World world;
    private Body body, leftBody;
    private Fixture fixture, leftFixture;

    public SoilEntity(World world, Texture texture, float x, float width, float y) {

        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(x + width / 2, y - 0.5f);
        body = world.createBody(def);
        BodyDef leftDef = new BodyDef();
        leftDef.position.set(x, y - 0.5f);
        leftBody = world.createBody(leftDef);

        PolygonShape soilShape = new PolygonShape();
        soilShape.setAsBox(width / 2, 0.5f);
        fixture = body.createFixture(soilShape, 1);
        fixture.setUserData("soil");
        soilShape.dispose();
        PolygonShape leftSoilShape = new PolygonShape();
        leftSoilShape.setAsBox(0.02f, 0.45f);
        leftFixture = leftBody.createFixture(leftSoilShape, 1);
        leftFixture.setUserData("block");
        leftSoilShape.dispose();

        setSize(width * PIXELS_IN_METER, PIXELS_IN_METER);
        setPosition(x * PIXELS_IN_METER, (y - 1) * PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
