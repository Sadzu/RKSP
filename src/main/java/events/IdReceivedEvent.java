package events;

import lombok.Getter;

@Getter
public class IdReceivedEvent {
    private final int id;
    public IdReceivedEvent(int id) { this.id = id; }
}
