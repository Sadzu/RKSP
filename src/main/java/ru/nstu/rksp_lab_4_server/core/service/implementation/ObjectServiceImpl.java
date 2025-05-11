package ru.nstu.rksp_lab_4_server.core.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nstu.rksp_lab_4_server.core.service.ObjectService;
import ru.nstu.rksp_lab_4_server.core.util.JsonStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectServiceImpl implements ObjectService {

    private final ArrayList<Integer> clients = new ArrayList<>();

    @Autowired
    private JsonStorage jsonStorage;

    @Override
    public Integer register() {
        if (clients.isEmpty()) {
            clients.add(1);
            return 1;
        }

        int newId = clients.getLast() + 1;
        clients.add(newId);
        return newId;
    }

    @Override
    public List<Integer> connections() {
        return clients;
    }

    @Override
    public Boolean send(Integer targetId, String json) {
        jsonStorage.save(targetId, json);
        return true;
    }

    @Override
    public String receive(String clientId) {
        return jsonStorage.getById(Integer.parseInt(clientId));
    }
}
