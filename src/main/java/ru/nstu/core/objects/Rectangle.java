package ru.nstu.core.objects;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Rectangle extends GraphicObject {
    private long width;
    private long height;

    public Rectangle(long x, long y, long width, long height, Color color) {
        super("Rectangle", x, y, color);
        this.width = width;
        this.height = height;
    }
}
