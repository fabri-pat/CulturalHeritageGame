package it.uniba.sms222325;

public class Constants {
    public static final float PIXELS_IN_METER = 45f;        // Scene2D usa i pixel (640*360), Box2D i metri (7.11*8), pongo che 8m di altezza siano 360px e se voglio trovare quanto vale 1m di y del protagonista, faccio 1*360/8 = 45
    public static final int IMPULSE_JUMP = 20;
    public static final int PLAYER_SPEED = 4;
    public static final int DENSITY = 3;        // massa protagonista
}
