package ru.nstu.core.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class GraphicObject {
    private String type;
    private long x;
    private long y;

    @Override
    public String toString() {
        return type + " " + x + " " + y;
    }
}
