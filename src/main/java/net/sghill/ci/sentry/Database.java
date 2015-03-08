package net.sghill.ci.sentry;

import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.domain.Builds;
import retrofit.client.Response;
import retrofit.http.*;

public interface Database {
    @PUT("/sentry")
    Response createDatabase();

    @PUT("/sentry/{id}")
    Response createBuild(@Path("id") String id, @Body Build build);

    @POST("/sentry/_bulk_docs")
    Response createBuilds(@Body Builds builds);

    @GET("/")
    Response ping();
}
