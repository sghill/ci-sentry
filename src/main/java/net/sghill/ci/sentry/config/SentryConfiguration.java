package net.sghill.ci.sentry.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import net.sghill.ci.sentry.validation.Enumeration;
import org.hibernate.validator.constraints.URL;
import retrofit.RestAdapter;

import javax.validation.Valid;
import java.util.Set;

@Getter
@ToString
public class SentryConfiguration {
    @Valid
    private final Set<Server> servers;
    @Valid
    private final CouchDb couchdb;

    public SentryConfiguration(@JsonProperty("servers") Set<Server> servers,
                               @JsonProperty("couchdb") CouchDb couchdb) {
        this.servers = servers;
        this.couchdb = couchdb;
    }

    @Getter
    @ToString
    public static class Server {
        @URL
        private final String baseUrl;
        @Enumeration(of = RestAdapter.LogLevel.class)
        private final String restLogLevel;

        public Server(@JsonProperty("baseUrl") String baseUrl,
                      @JsonProperty("restLogLevel") String restLogLevel) {
            this.baseUrl = baseUrl;
            this.restLogLevel = restLogLevel;
        }
    }

    @Getter
    @ToString
    public static class CouchDb {
        @URL
        private final String baseUrl;

        public CouchDb(@JsonProperty("baseUrl") String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
