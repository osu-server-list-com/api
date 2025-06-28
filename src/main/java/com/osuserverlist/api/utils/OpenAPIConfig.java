package com.osuserverlist.api.utils;

import java.util.function.Consumer;

import com.osuserverlist.shared.Globals;

import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;

public class OpenAPIConfig extends OpenApiPlugin {

    public OpenAPIConfig(Consumer<OpenApiPluginConfiguration> userConfig) {
        super(userConfig.andThen(config -> {
            config.withDefinitionConfiguration((version, definition) -> {
                definition.withInfo(info -> {
                    info.title("osu!Server List API")
                            .version("4.0.0")
                            .description("The next level API for osu!Server List");
                });
                definition.withServer(server -> {
                    server.description("Server")
                            .url(Globals.serverConfig.api.domain + ":" + Globals.serverConfig.api.port);
                });
            });
        }));
    }
}
