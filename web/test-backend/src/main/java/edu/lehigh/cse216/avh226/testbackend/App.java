package edu.lehigh.cse216.avh226.testbackend;

// Import the Javalin package, so that we can make use of the "get" function to 
// create an HTTP GET route
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Scanner;
import io.javalin.http.staticfiles.Location;
// Import Google's JSON library
import com.google.gson.*;

public class App {

    public static final int DEFAULT_PORT_WEBSERVER = 8000;

    /**
     * Safely gets integer value from named env var if it exists, otherwise returns
     * default
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        if (envar == null || envar.length() == 0 || System.getenv(envar.trim()) == null)
            return defaultVal;
        try (Scanner sc = new Scanner(System.getenv(envar.trim()))) {
            if (sc.hasNextInt())
                return sc.nextInt();
            else
                System.err.printf("ERROR: Could not read %s from environment, using default of %d%n", envar,
                        defaultVal);
        }
        return defaultVal;
    }

    public static void main(String[] args) {
        main_hardcoded_responses(args);
        // main_uses_database(args);
    }

    public static void main_hardcoded_responses(String[] args) {

        ArrayList<Database.RowData> test_msgs = new ArrayList<Database.RowData>();
        test_msgs.add(new Database.RowData(1, 0, "test 1"));
        test_msgs.add(new Database.RowData(2, 1, "test 2"));
        test_msgs.add(new Database.RowData(3, 0, "test 3"));
        test_msgs.add(new Database.RowData(4, 50, "test 4: this one's bigger!"));

        Javalin app = Javalin
                .create(
                        config -> {
                            config.staticFiles.add(staticFiles -> {
                                staticFiles.hostedPath = "/"; // change to host files on a subpath, like '/assets'
                                String static_location_override = System.getenv("STATIC_LOCATION");
                                if (static_location_override == null) { // serve from jar; files located in
                                                                        // src/main/resources/public
                                    staticFiles.directory = "/public/build"; // the directory where your
                                    // files are
                                    // located
                                    staticFiles.location = Location.CLASSPATH; // Location.CLASSPATH (jar)
                                } else { // serve from filesystem
                                    System.out.println(
                                            "Overriding location of static file serving using STATIC_LOCATION env var: "
                                                    + static_location_override);
                                    staticFiles.directory = static_location_override; // the directory where your files
                                                                                      // are located
                                    staticFiles.location = Location.EXTERNAL; // Location.EXTERNAL (file system)
                                }
                                staticFiles.precompress = false; // if the files should be pre-compressed and cached in
                                                                 // memory (optimization)
                                // staticFiles.aliasCheck = null; // you can configure this to enable symlinks
                                // (= ContextHandler.ApproveAliases())
                                // staticFiles.headers = Map.of(...); // headers that will be set for the files
                                // staticFiles.skipFileFunction = req -> false; // you can use this to skip
                                // certain files in the dir, based on the HttpServletRequest
                                // staticFiles.mimeTypes.add(mimeType, ext); // you can add custom mimetypes for
                                // extensions
                            });
                            config.requestLogger.http(
                                    (ctx, ms) -> {
                                        System.out.printf("%s%n", "=".repeat(42));
                                        System.out.printf("%s\t%s\t%s%nfull url: %s%n", ctx.scheme(),
                                                ctx.method().name(), ctx.path(), ctx.fullUrl());
                                    });
                        });

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        app.get("/messages", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse("ok", null, test_msgs);
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        // PUT route for updating a row in the DataStore. This is almost exactly the
        // same as POST
        app.put("/messages/{id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server
            // Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            // NB: update entry in MockDataStore; updateOne checks for null title and
            // message and invalid ids
            test_msgs.set(idx - 1, new Database.RowData(idx, req.mLikes(), req.mMessage()));
            // int result = db.updateOne(idx, req.mLikes(), req.mMessage());
            int result = 1;
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, result);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        app.put("/messages/{id}/like/increment", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // NB: update entry in MockDataStore; updateOne checks for null title and
            // message and invalid ids
            Database.RowData old_msg = test_msgs.get(idx - 1);
            test_msgs.set(idx - 1, new Database.RowData(idx, old_msg.mLikes() + 1, old_msg.mMessage()));
            // int result = db.updateOne(idx, req.mLikes(), req.mMessage());
            int result = 1;
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, test_msgs.get(idx - 1));
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        app.put("/messages/{id}/like/decrement", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // NB: update entry in MockDataStore; updateOne checks for null title and
            // message and invalid ids
            Database.RowData old_msg = test_msgs.get(idx - 1);
            test_msgs.set(idx - 1, new Database.RowData(idx, old_msg.mLikes() - 1, old_msg.mMessage()));
            // int result = db.updateOne(idx, req.mLikes(), req.mMessage());
            int result = 1;
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, test_msgs.get(idx - 1));
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // DELETE route for removing a row from the MockDataStore
        app.delete("/messages/{id}", ctx -> {
            // If we can't get an ID or can't parse the JSON, javalin sends a status 500
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            test_msgs.remove(idx - 1);
            // int result = db.deleteRow(idx);
            int result = 1;
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, "deleted row " + idx);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        // GET route that returns everything for a single row in the MockDataStore.
        // The "{id}" suffix in the first parameter to get() becomes
        // ctx.pathParam("id"), so that we can get the requested row ID. If
        // "{id}" isn't a number, Javalin will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        app.get("/messages/{id}", ctx -> {
            // NB: the {} syntax "/messages/{id}" does not allow slashes ('/') as part of
            // the parameter
            // NB: the <> syntax "/messages/<id>" allows slashes ('/') as part of the
            // parameter
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            Database.RowData data = test_msgs.get(idx - 1);
            // Database.RowData data = db.selectOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data);
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        app.get("/messages/{id}/like", ctx -> {
            // NB: the {} syntax "/messages/{id}" does not allow slashes ('/') as part of
            // the parameter
            // NB: the <> syntax "/messages/<id>" allows slashes ('/') as part of the
            // parameter
            int idx = Integer.parseInt(ctx.pathParam("id"));

            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON

            Database.RowData data = test_msgs.get(idx - 1);
            // Database.RowData data = db.selectOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data.mLikes());
            }

            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        // POST route for adding a new element to the MockDataStore. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.
        app.post("/messages", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the
            // error.
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server
            // Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            // NB: add to database; insertRow method DOES NOT check for null title and
            // message
            // however, our "create table" sql command does specify a "NOT NULL" constraint
            // int newId = dataStore.createEntry(req.mTitle(), req.mMessage());

            int newId = test_msgs.size() + 1;
            test_msgs.add(new Database.RowData(newId, 0, req.mMessage()));
            // int newId = db.insertRow(0, req.mMessage());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (title or message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), test_msgs.get(newId - 1));
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });
        // don't forget: nothing happens until we `start` the server
        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER));
    }

    public static void main_uses_database(String[] args) {

    }
}
