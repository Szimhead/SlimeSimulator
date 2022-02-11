import java.util.Random;

public class SlimeCell {

    private static final double TWO_PI = 6.28;
    private double posX;
    private double posY;
    private double angle;
    private final Configuration configuration;

    public SlimeCell(Configuration configuration, int posX, int posY, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.configuration = configuration;
    }

    public int getPosX() {
        return (int) Math.round(posX);
    }

    public int getPosY() {
        return (int) Math.round(posY);
    }

    public double getAngle() {
        return angle;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void move() {
        posX += Math.cos(angle)*configuration.SPEED;
        posY += Math.sin(angle)* configuration.SPEED;
        int outsideWindowX = isOutsideWindowX();
        int outsideWindowY = isOutsideWindowY();

        if (outsideWindowX != 0 || outsideWindowY != 0) {
            posX = Math.min(configuration.B_WIDTH - configuration.SIGHT -1, Math.max(configuration.SIGHT +1, posX));
            posY = Math.min(configuration.B_HEIGHT - configuration.SIGHT -1, Math.max(configuration.SIGHT +1, posY));

            getAngleFrom();
        }
    }

    private void getAngleFrom() {
        angle = new Random().nextFloat() * TWO_PI;
    }

    int isOutsideWindowX() {
        if (posX < configuration.SIGHT -1) {
            return -1;
        }
        if (posX > configuration.B_WIDTH- configuration.SIGHT -1) {
            return 1;
        }
        return 0;
    }

    int isOutsideWindowY() {
        if (posY < configuration.SIGHT -1) {
            return -1;
        }
        if (posY > configuration.B_HEIGHT- configuration.SIGHT -1) {
            return 1;
        }
        return 0;
    }
}
