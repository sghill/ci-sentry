package net.sghill.ci.sentry;

import net.sghill.ci.jenkins.api.JenkinsJob;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface JenkinsService {
    @GET("/job/{name}/api/json?depth=1")
    JenkinsJob fetchJobByName(@Path("name") String name);

    @GET("/api/json")
    Response ping();
}
