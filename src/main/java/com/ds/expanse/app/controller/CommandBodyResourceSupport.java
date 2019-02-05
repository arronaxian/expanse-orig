package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

public class CommandBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private String result;

    public CommandBodyResourceSupport(CommandResult result) {
        this.result = result.toString();
    }
}
