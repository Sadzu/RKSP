package ru.nstu.core.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Circle extends GraphicObject {
    private long radius;

    public Circle(long x, long y, long radius) {
        super("Circle", x, y);
        this.radius = radius;
    }
}
