package com.ds.expanse.app.api.service;

import com.ds.expanse.app.api.controller.model.MapElements;
import com.ds.expanse.app.repository.MapElementsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

public interface MapElementsService {
    void saveMap(String name, InputStream mapElementsStream, String startHereRoomName);
}
