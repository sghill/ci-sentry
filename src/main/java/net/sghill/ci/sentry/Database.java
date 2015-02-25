package net.sghill.ci.sentry;

import net.sghill.ci.sentry.domain.Build;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;

import java.util.UUID;

public interface Database {
    @PUT("/sentry")
    Response createDatabase();

    @PUT("/sentry/{id}")
    Response createBuild(@Path("id") UUID id, @Body Build build);
}
