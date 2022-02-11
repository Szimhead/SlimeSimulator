

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ImageManager extends JPanel
        implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ImageManager.class.getName());

    public final Configuration configuration;

    private final int DELAY = 20;

    private static int step = 0;
    private static long time;
    private double range;

    private Slime slimeOld;

    private Thread animator;
    private BufferedImage image;

    public ImageManager(Configuration configuration) {
        this.configuration = configuration;
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(configuration.B_WIDTH, configuration.B_HEIGHT));

        image = new BufferedImage(configuration.B_WIDTH, configuration.B_HEIGHT,TYPE_INT_RGB);

        slimeOld = new Slime(configuration, "Blue");
        range = 1.0/ configuration.MAX_INTENSITY;
        time = System.nanoTime();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawStar(g);
    }

    private void drawStar(Graphics g) {
        Trail[][] trailsOld = slimeOld.getTrailMap();
        progress();
        for (int i = 0; i < configuration.B_WIDTH; i++) {
            for (int j = 0; j < configuration.B_HEIGHT; j++) {
                double intensity;
                double intensityOld = toRange01(trailsOld[i][j].getIntensity());
                Color current;
                    current =  trailsOld[i][j].getColor();
                    intensity = intensityOld;
                Color color = new Color((int)(current.getRed() * intensity),
                            (int)(current.getGreen() * intensity),
                            (int)(current.getBlue() * intensity),
                            current.getAlpha());
                image.setRGB(i, j, color.getRGB());
            }
        }
        g.drawImage(image,0, 0,this);
        Toolkit.getDefaultToolkit().sync();
    }

    private void progress() {
        step++;
        slimeOld.moveCells(step);
        slimeOld.evaporateTrails();
    }

    private double toRange01(int intensity) {
        return range * intensity;
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        while (true) {
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread interrupted: %s", e.getMessage());
                JOptionPane.showMessageDialog(this, msg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}
