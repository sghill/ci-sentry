package net.sghill.ci.sentry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;

@ToString
public class SentryConfiguration {
    @Valid
    @Getter
    private final Server server;
    @Valid
    @Getter
    private final CouchDb couchdb;

    public SentryConfiguration(@JsonProperty("server") Server server,
                               @JsonProperty("couchdb") CouchDb couchdb) {
        this.server = server;
        this.couchdb = couchdb;
    }

    @ToString
    public static class Server {
        @URL
        @Getter
        private final String baseUrl;

        public Server(@JsonProperty("baseUrl") String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    @ToString
    public static class CouchDb {
        @URL
        @Getter
        private final String baseUrl;

        public CouchDb(@JsonProperty("baseUrl") String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
