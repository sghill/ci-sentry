package net.sghill.ci.sentry.domain;

import com.google.common.collect.Maps;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.support.Views;

import javax.inject.Inject;
import java.util.Map;

@Views({
        @View(name = "all", map = "function(doc){if(doc.type === 'BUILD'){emit(null,doc._id)}}"),
        @View(name = "most_recent",
                map = "function(doc){emit(doc.name,doc.run);}",
                reduce = "function(k,v,rereduce){var max = 0; v.forEach(function(x){if(x > max) { max = x; }}); return max;}"
        )
})
public class BuildRepository extends CouchDbRepositorySupport<Build> {
    @Inject
    public BuildRepository(CouchDbConnector db) {
        super(Build.class, db);
        initStandardDesignDocument();
    }

    public Map<String, Long> findMostRecent() {
        ViewQuery mostRecent = createQuery("most_recent").groupLevel(1);
        ViewResult result = db.queryView(mostRecent);
        Map<String, Long> map = Maps.newHashMap();
        for (ViewResult.Row row : result.getRows()) {
            map.put(row.getKey(), row.getValueAsNode().asLong());
        }
        return map;
    }
}
