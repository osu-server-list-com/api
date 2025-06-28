package com.osuserverlist.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osuserverlist.shared.Globals;
import com.osuserverlist.shared.database.Database;

import io.javalin.Javalin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

import com.osuserverlist.api.controllers.CategoryController;
import com.osuserverlist.api.utils.JavalinJsonMapper;
import com.osuserverlist.api.utils.OpenAPIConfig;

/**
 * Hello world!
 *
 */
public class App {
    static Logger logger = LoggerFactory.getLogger("OSL-API");
    static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        logger.info("Osu Server List - Javalin API - Developement Build!");  
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new OpenAPIConfig(openApiConf -> {
                logger.info("Configuring OpenAPI specification");
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());
            config.jsonMapper(new JavalinJsonMapper());
        });

        Database.connect(Globals.serverConfig);

        Globals.dsl.fetch("SELECT 1").forEach(record -> {
            logger.info("Database connection test successful: " + record.get(0));
        });
        
        // Configure routes
        app.get("/api/v3/categories", CategoryController::getAllCategories);
        app.get("/api/v3/categories/{id}", CategoryController::getCategoryById);

        app.start(Globals.serverConfig.api.port);
        logger.info("Osu Server List API started on port: " + Globals.serverConfig.api.domain + ":" + Globals.serverConfig.api.port);
        logger.info("-> Ignited in " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
