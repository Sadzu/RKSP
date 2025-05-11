package ru.nstu.rksp_lab_4_server.core.util;

public interface JsonStorage {

    String getById(Integer clientId);

    void save(Integer clientId, String json);

}
