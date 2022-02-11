import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadAnimation extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ThreadAnimation.class.getName());

    public ThreadAnimation() {
        Configuration config = readConfig();
        initUI(config);
    }

    private Configuration readConfig() {
        Configuration config;
        String rootPath = "";
        String appConfigPath = rootPath + "app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
            config = new Configuration(
                    Integer.parseInt(appProps.getProperty("field_width")),
                    Integer.parseInt(appProps.getProperty("field_height")),
                    Integer.parseInt(appProps.getProperty("slime_size")),
                    Integer.parseInt(appProps.getProperty("initial_x")),
                    Integer.parseInt(appProps.getProperty("initial_y")),
                    Float.parseFloat(appProps.getProperty("cell_speed")),
                    Integer.parseInt(appProps.getProperty("step_to_evolve")),
                    Double.parseDouble(appProps.getProperty("turn_angle")),
                    Integer.parseInt(appProps.getProperty("sight")),
                    Integer.parseInt(appProps.getProperty("max_colour_intensity")),
                    Shape.valueOf(appProps.getProperty("shape")),
                    Integer.parseInt(appProps.getProperty("density")),
                    Integer.parseInt(appProps.getProperty("radius")),
                    new Color(Integer.parseInt(appProps.getProperty("red")),
                            Integer.parseInt(appProps.getProperty("green")),
                            Integer.parseInt(appProps.getProperty("blue"))),
                    Integer.parseInt(appProps.getProperty("random_factor")),
                    Double.parseDouble(appProps.getProperty("angle_split"))


                    );
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load configuration from file. Using default configuration");
            return new Configuration();
        }
        return config;
    }


    private void initUI(Configuration configuration) {

        add(new ImageManager(configuration));

        setResizable(false);
        pack();

        setTitle("SlimeSimulator");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new ThreadAnimation();
            ex.setVisible(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}
