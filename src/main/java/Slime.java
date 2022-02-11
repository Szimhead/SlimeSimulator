import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Slime {


    private List<SlimeCell> slimeCells;
    private final Trail[][] trailMap;
    private final Configuration configuration;

    private final double turnAngleWidth;
    private final double turnAngleStep;
    private final double minRandomAngle;
    private final double maxRandomAngle;
    private Trail[][] previousTrails;

    private final Random random = new Random();

    public Slime(Configuration configuration, String species) {
        this.configuration = configuration;
        slimeCells = new ArrayList<>(configuration.SLIME_SIZE);
        trailMap = new Trail[configuration.B_WIDTH][configuration.B_HEIGHT];
        previousTrails = new Trail[configuration.B_WIDTH][configuration.B_HEIGHT];
        createTrailMap(species, configuration.COLOR, configuration.B_WIDTH, configuration.B_HEIGHT);
        if (configuration.SHAPE == Shape.circle_field){
            createSlimeCircle(species, configuration.COLOR);
        } else if (configuration.SHAPE == Shape.point) {
            createSlimePoint();
        }
        turnAngleWidth = Math.PI/configuration.TURN_ANGLE;
        turnAngleStep = 2*Math.PI/configuration.TURN_ANGLE/configuration.ANGLE_SPLIT;
        minRandomAngle = 360.0/configuration.TURN_ANGLE;
        maxRandomAngle = 2.0 * 360.0/ configuration.TURN_ANGLE;
    }

    private void createSlimePoint() {
        double angle = 1.0/configuration.SLIME_SIZE * 2 * Math.PI;
        for (int i = 0; i < configuration.SLIME_SIZE; i++) {
            SlimeCell newCell = new SlimeCell(configuration, configuration.INITIAL_X, configuration.INITIAL_Y, angle);
            angle+= 1.0/configuration.SLIME_SIZE * 2 * Math.PI;
            slimeCells.add(newCell);
        }
    }

    private void createSlimeCircle(String species, Color color) {
        slimeCells = new ArrayList<>();
        for (int i = configuration.INITIAL_X - configuration.RADIUS; i < configuration.INITIAL_X+configuration.RADIUS; i+=configuration.DENSITY) {
            for (int j = configuration.INITIAL_Y - configuration.RADIUS; j < configuration.INITIAL_Y + configuration.RADIUS; j+=configuration.DENSITY) {
                if ((i - configuration.INITIAL_X)*(i - configuration.INITIAL_X) + (j - configuration.INITIAL_Y)*(j - configuration.INITIAL_Y) < configuration.RADIUS*configuration.RADIUS) {
                    double angle = Math.atan2(configuration.INITIAL_Y -j , configuration.INITIAL_X - i);
                    SlimeCell newCell = new SlimeCell(configuration, i, j, angle);
                    slimeCells.add(newCell);
                }
            }
        }
    }

    private void createTrailMap(String species, Color color, int mapWidth, int mapHeight) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                trailMap[i][j] = new Trail(species, Trail.MIN_INTENSITY, color);
                previousTrails[i][j]= trailMap[i][j];
            }
        }
    }

    public List<SlimeCell> getSlimeCells() {
        return slimeCells;
    }

    public Trail[][] getTrailMap() {
        return trailMap;
    }

    public void setSlimeCells(List<SlimeCell> slimeCells) {
        this.slimeCells = slimeCells;
    }

    public void moveCells(int step) {
        slimeCells.parallelStream().forEach(slimeCell -> {
            if (step > configuration.STEP_TO_EVOLVE) {
                double angle = slimeCell.getAngle();
                slimeCell.setAngle(findMaxIntensityForAngleRange(angle, slimeCell));
            }
            slimeCell.move();
            leaveTrails(slimeCell);
        });
    }

    private double findMaxIntensityForAngleRange(double currentAngle, SlimeCell cell) {
        int maxIntensity = 0;
        double maxAngle = currentAngle;
        int intensity;
        for(double angle = currentAngle - turnAngleWidth; angle < currentAngle + turnAngleWidth; angle += turnAngleStep) {
            intensity = 0;
            for (int i = 0; i < configuration.SIGHT; i++) {
                if (cell.getPosX() > configuration.SIGHT && cell.getPosX() < configuration.B_WIDTH - configuration.SIGHT && cell.getPosY() > configuration.SIGHT && cell.getPosY() < configuration.B_HEIGHT - configuration.SIGHT) {
                    int x = (int) Math.round(cell.getPosX() + i * Math.cos(angle));
                    int y = (int) Math.round(cell.getPosY() + i * Math.sin(angle));
                    intensity += trailMap[x][y].getIntensity();
                    if(intensity > maxIntensity) {
                        maxIntensity = intensity;
                        maxAngle = angle;
                    }
                }
            }
            if(intensity > maxIntensity) {
                maxIntensity = intensity;
                maxAngle = angle;
            }
        }
        if (random.nextInt(100) > configuration.RANDOM_FACTOR) {
            double r = Math.toRadians(-minRandomAngle + random.nextDouble() * maxRandomAngle);
            return currentAngle + r;
        }
        return maxAngle;
    }

    public void leaveTrails(SlimeCell slimeCell) {
        trailMap[slimeCell.getPosX()][slimeCell.getPosY()].setIntensity(configuration.MAX_INTENSITY);
    }

    public void evaporateTrails() {
        IntStream.range(1,  trailMap.length-1).parallel().forEach(i -> {
            IntStream.range(1,  trailMap[0].length-1).parallel().forEach(j -> {
                int average = 0;
                int currentIntensity = previousTrails[i][j].getIntensity();
                if (currentIntensity > 2) {
                    previousTrails[i][j].setIntensity(currentIntensity-1);
                } else {
                    previousTrails[i][j].setIntensity(0);
                }
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        average += previousTrails[i+k][j+l].getIntensity();
                    }
                }
                average = (int) (average*0.111);

                trailMap[i][j].setIntensity(average);
            });
        });
        previousTrails = trailMap;
    }
}
