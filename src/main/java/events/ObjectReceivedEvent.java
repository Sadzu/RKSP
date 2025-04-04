package events;

import lombok.Getter;
import com.google.common.eventbus.Subscribe;

@Getter
public class ObjectReceivedEvent {
    private String object;

    public ObjectReceivedEvent(String object) {
        this.object = object;
    }
}
