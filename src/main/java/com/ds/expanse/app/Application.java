package com.ds.expanse.app;

import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class Application {
    @Autowired
    protected Loader loader;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();

            System.exit(1);
        }
    }
}