package ru.nstu.core.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class GraphicObject {
    private String type;
    private long x;
    private long y;
    private Color color;

    @Override
    public String toString() {
        return type + " " + x + " " + y;
    }
}
