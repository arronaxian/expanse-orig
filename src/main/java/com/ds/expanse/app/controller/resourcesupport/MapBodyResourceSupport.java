package com.ds.expanse.app.controller.resourcesupport;

import com.ds.expanse.app.command.CommandResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

public class MapBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private String result;

    public MapBodyResourceSupport(String result) {
        this.result = result;
    }
}
