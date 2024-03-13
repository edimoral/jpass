package jpass.util;

import java.awt.Color;

public final class ColorGradient {

    // Define key color points
    private static final Color[] COLORS = {
            new Color(107, 1, 1),      // 0
            new Color(211, 9, 9),    // 25
            new Color(255, 102, 0),    // 50
            new Color(71, 218, 71),    // (75)
            new Color(0, 102, 0)       // 100
    };

    private ColorGradient() {

    }

    private static Color interpolateColor(Color start, Color end, double fraction) {
        // Calculate the difference in each color component
        int red = (int) Math.round((end.getRed() - start.getRed()) * fraction + start.getRed());
        int green = (int) Math.round((end.getGreen() - start.getGreen()) * fraction + start.getGreen());
        int blue = (int) Math.round((end.getBlue() - start.getBlue()) * fraction + start.getBlue());

        return new Color(red, green, blue);
    }

    public static Color getGradientColor(double score) {
        // Find the fraction and color range the score falls into
        int index = (int) score / 25; // Determine the starting color index
        if (index >= COLORS.length - 1) {
            return COLORS[COLORS.length - 1]; // Return green if at the end
        }

        double fraction = (score % 25) / 25.0; // Fraction through the current color range
        return interpolateColor(COLORS[index], COLORS[index + 1], fraction);
    }
}
