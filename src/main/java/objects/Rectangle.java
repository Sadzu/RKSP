package objects;

import lombok.*;

@Getter
@Setter
public class Rectangle extends GraphicObject {
    private long width;
    private long height;

    public Rectangle(long x, long y, long width, long height) {
        super("Rectangle", x, y);
        this.width = width;
        this.height = height;
    }
}
