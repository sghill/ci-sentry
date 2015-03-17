package net.sghill.ci.sentry;

import net.sghill.ci.jenkins.api.Jenkins;
import net.sghill.ci.jenkins.api.JenkinsJob;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface JenkinsService {
    @GET("/api/json?tree=jobs[name,builds[building,duration,number,result,timestamp]]")
    Jenkins fetchAllJobs();

    @GET("/api/json")
    Response ping();
}
