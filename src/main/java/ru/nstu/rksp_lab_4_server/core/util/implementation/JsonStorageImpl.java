package ru.nstu.rksp_lab_4_server.core.util.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nstu.rksp_lab_4_server.core.util.JsonStorage;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JsonStorageImpl implements JsonStorage {

    private final HashMap<Integer, String> clientsData = new HashMap<>();

    @Override
    public String getById(Integer clientId) {
        if (clientsData.containsKey(clientId)) {
            String clientData = clientsData.get(clientId);
            clientsData.remove(clientId);
            return clientData;
        }

        return null;
    }

    @Override
    public void save(Integer clientId, String json) {
        clientsData.put(clientId, json);
    }
}
