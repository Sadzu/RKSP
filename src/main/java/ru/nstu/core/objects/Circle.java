package ru.nstu.core.objects;

import lombok.Getter;
import lombok.Setter;

import java.awt.Color;

@Getter
@Setter
public class Circle extends GraphicObject {
    private long radius;

    public Circle(long x, long y, long radius, Color color) {
        super("Circle", x, y, color);
        this.radius = radius;
    }
}
