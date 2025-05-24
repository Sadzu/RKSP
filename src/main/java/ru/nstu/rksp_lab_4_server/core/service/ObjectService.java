package ru.nstu.rksp_lab_4_server.core.service;

import java.util.List;

public interface ObjectService {

    Integer register();

    List<Integer> connections();

    Boolean send(Integer targetId, String json);

    String receive(String clientId);

    String getAll(Integer clientId);

    Boolean putNew(Integer clientId, String json);

}
