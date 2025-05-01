package objects;

import lombok.*;

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
