package net.sghill.ci.sentry.cli.actions.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import net.sghill.ci.sentry.domain.Build;
import org.ektorp.CouchDbConnector;
import org.ektorp.StreamingViewResult;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.SortedMap;

public class FailuresReportAction implements Runnable {
    private final CouchDbConnector db;
    private final ObjectMapper objectMapper;
    private final Logger logger;
    private final StatsFormatter formatter;

    @Inject
    public FailuresReportAction(CouchDbConnector connector, @Named("JSON") ObjectMapper objectMapper, Logger logger, StatsFormatter formatter) {
        this.db = connector;
        this.objectMapper = objectMapper;
        this.logger = logger;
        this.formatter = formatter;
    }

    @Override
    public void run() {
        ViewQuery viewQuery = new ViewQuery()
                .designDocId("_design/Build")
                .viewName("all")
                .includeDocs(true);
        StreamingViewResult rows = db.queryForStreamingView(viewQuery);
        SortedMap<String, Stats> statsByBuild = Maps.newTreeMap();
        for (ViewResult.Row row : rows) {
            try {
                Build build = objectMapper.readValue(row.getDoc(), Build.class);
                Stats stats = MoreObjects.firstNonNull(statsByBuild.get(build.getName()), new Stats(0, 0));
                statsByBuild.put(build.getName(), stats.addBuild(build));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info(formatter.format(statsByBuild));
    }

}
