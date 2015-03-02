package net.sghill.ci.sentry;

import net.sghill.ci.sentry.domain.Build;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface Database {
    @PUT("/sentry")
    Response createDatabase();

    @PUT("/sentry/{id}")
    Response createBuild(@Path("id") String id, @Body Build build);

    @GET("/")
    Response ping();
}
