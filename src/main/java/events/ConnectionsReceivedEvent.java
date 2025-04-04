package events;

import lombok.Getter;

@Getter
public class ConnectionsReceivedEvent {

    private String connections;

    public ConnectionsReceivedEvent(String connections) {
        this.connections = connections;
    }
}
