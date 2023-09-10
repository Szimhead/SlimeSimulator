import java.awt.*;

public class Configuration {
    public final int B_WIDTH;
    public final int B_HEIGHT;
    public final int SLIME_SIZE;
    public int INITIAL_X;
    public int INITIAL_Y;
    public float SPEED;
    public final int STEP_TO_EVOLVE;
    public final double TURN_ANGLE;
    public final int SIGHT;
    public final int MAX_INTENSITY;
    public final Shape SHAPE;
    public final int DENSITY;
    public final int RADIUS;
    public Color COLOR;
    public final int RANDOM_FACTOR;
    public final double ANGLE_SPLIT;

    public Configuration(int width,
                         int height,
                         int slimeSize,
                         int initialX,
                         int initialY,
                         float speed,
                         int stepToEvolve,
                         double turnAngle,
                         int sight,
                         int maxIntensity,
                         Shape shape,
                         int density,
                         int radius,
                         Color color,
                         int randomFactor,
                         double angleSplit) {
        B_WIDTH = width;
        B_HEIGHT = height;
        SLIME_SIZE = slimeSize;
        INITIAL_X = initialX;
        INITIAL_Y = initialY;
        SPEED = speed;
        STEP_TO_EVOLVE = stepToEvolve;
        TURN_ANGLE = turnAngle;
        SIGHT = sight;
        MAX_INTENSITY = maxIntensity;
        SHAPE = shape;
        DENSITY = density;
        RADIUS = radius;
        COLOR = color;
        RANDOM_FACTOR = randomFactor;
        ANGLE_SPLIT = angleSplit;
    }

    public Configuration() {
        B_WIDTH = 1000;
        B_HEIGHT = 1000;
        SLIME_SIZE = 100000;
        INITIAL_X = 450;
        INITIAL_Y = 450;
        SPEED = 2F;
        STEP_TO_EVOLVE = 500;
        TURN_ANGLE = 6F;
        SIGHT = 100;
        MAX_INTENSITY = 60;
        SHAPE = Shape.point;
        DENSITY = 2;
        RADIUS = 100;
        COLOR = Color.WHITE;
        RANDOM_FACTOR = 60;
        ANGLE_SPLIT = 2;
    }


}
