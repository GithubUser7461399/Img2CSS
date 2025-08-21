import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class ColorCluster {
    Color color;
    List<Pixel> pixels;

    ColorCluster(Color color) {
        this.color = color;
        this.pixels = new ArrayList<>();
    }

    void addPixel(Pixel pixel) {
        pixels.add(pixel);
    }

    void clearPixels() {
        pixels.clear();
    }

    Color calculateAverageColor() {
        int r = 0, g = 0, b = 0;
        for (Pixel pixel : pixels) {
            r += pixel.r;
            g += pixel.g;
            b += pixel.b;
        }
        int size = pixels.size();
        return new Color(r / size, g / size, b / size);
    }
}