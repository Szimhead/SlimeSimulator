import java.awt.*;

public class Trail {
    public static final int MIN_INTENSITY = 0;

    private String species;
    private int intensity;
    private Color color;

    public Trail(String species, int intensity, Color color) {
        this.species = species;
        this.intensity = intensity;
        this.color = color;
    }

    public String getSpecies() {
        return species;
    }

    public int getIntensity() {
        return intensity;
    }

    public Color getColor() {
        return color;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
