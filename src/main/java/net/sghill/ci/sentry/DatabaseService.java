package net.sghill.ci.sentry;

import net.sghill.ci.sentry.cli.actions.init.InitDbResult;
import retrofit.RetrofitError;

import javax.inject.Inject;

public class DatabaseService {
    private final Database database;

    @Inject
    public DatabaseService(Database database) {
        this.database = database;
    }

    public InitDbResult ensureDatabaseExists() {
        try {
            database.createDatabase();
            return InitDbResult.CREATED;
        } catch (RetrofitError e) {
            if(e.getResponse().getStatus() == 412) {
                return InitDbResult.ALREADY_EXISTED;
            }
            return InitDbResult.ERROR;
        }
    }
}
