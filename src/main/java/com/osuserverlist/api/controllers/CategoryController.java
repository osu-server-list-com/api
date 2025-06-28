package com.osuserverlist.api.controllers;

import java.util.List;

import com.osuserverlist.shared.Globals;
import com.osuserverlist.shared.database.records.Category;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;

public class CategoryController {
    @OpenApi(
        summary = "Get All Categories", 
        description = "Returns a list of all categories", 
        path = "/api/v3/categories", 
        methods = HttpMethod.GET, 
        tags = {
            "Categories" 
        }, 
        responses = {
                    @OpenApiResponse(
                        status = "200", 
                        content = {
                            @OpenApiContent(from = Category[].class) 
                        }, 
                        description = "Successful response with list of all categories")
            })
    public static void getAllCategories(Context ctx) {
        List<Category> categories = Globals.dsl
                .select(Globals.CATEGORY.ID, Globals.CATEGORY.NAME, Globals.CATEGORY.COLOR)
                .from(Globals.CATEGORY)
                .fetchInto(Category.class);
        ctx.json(categories);
    }

    @OpenApi(
                summary = "Get Category by ID",
                description = "Returns a single category by its ID",
                path = "/api/v3/categories/{id}",
                methods = HttpMethod.GET,
                tags = {"Categories"},
                pathParams = {
                        @OpenApiParam(
                                name = "id",
                                description = "The ID of the category to retrieve",
                                type = Integer.class,
                                required = true
                        )
                },
                responses = {
                        @OpenApiResponse(
                                status = "200",
                                content = {@OpenApiContent(from = Category.class)},
                                description = "Successful response with the requested category"
                        ),
                        @OpenApiResponse(
                                status = "400",
                                description = "Invalid ID format. ID must be a valid integer."
                        ),
                        @OpenApiResponse(
                                status = "404",
                                description = "Category not found"
                        )
                }
        )
    public static void getCategoryById(Context ctx) {
        String idParam = ctx.pathParam("id");

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format. ID must be a valid integer.");
            return;
        }

        Category category = Globals.dsl
                .selectFrom(Globals.CATEGORY)
                .where(Globals.CATEGORY.ID.eq(id))
                .fetchOneInto(Category.class);
                
        if (category != null) {
            ctx.json(category);
        } else {
            ctx.status(404).result("Category not found");
        }
    }
}
