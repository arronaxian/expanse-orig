package com.ds.expanse.app.api.command;

/**
 * Executes the provided request and returns a response.
 * @param <Req> The command request type.
 * @param <Res> The command response type.
 */
public interface Command<Req, Res> {

    /**
     * Executes the command request.
     *
     * @param request The request to execute.
     * @return The response to the command.
     */
    Res execute(Req request);
}
