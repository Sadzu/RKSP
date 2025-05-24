package ru.nstu.core.objects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public final class Colors {
    private static final ArrayList<Color> colors = new ArrayList<>(
            Arrays.asList(
                    Color.BLACK,
                    Color.MAGENTA,
                    Color.GREEN,
                    Color.BLUE,
                    Color.YELLOW,
                    Color.RED,
                    Color.CYAN,
                    Color.LIGHT_GRAY
                    ));

    public static Color getColor(int clientId) {
        return colors.get(clientId % colors.size());
    }
}
