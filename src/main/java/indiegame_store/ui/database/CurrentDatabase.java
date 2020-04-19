package indiegame_store.ui.database;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import indiegame_store.backend.database.Database;
import indiegame_store.backend.database.DatabaseFactory;

public final class CurrentDatabase {

    private static final String CURRENT_DATABASE_ATTRIBUTE_KEY = CurrentDatabase.class.getCanonicalName();

    private CurrentDatabase() {
    }

    /**
     * Get's current database instance from session
     *
     * @return Database instance
     */
    public static Database get() {
        return (Database) getCurrentRequest().getWrappedSession().getAttribute(CURRENT_DATABASE_ATTRIBUTE_KEY);
    }

    /**
     * Creates database instance and set's it to current active session
     *
     * @param databasePath database file path
     */
    public static boolean set(String databasePath) {
        if (databasePath == null) {
            getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_DATABASE_ATTRIBUTE_KEY);
            return false;
        } else {
            Database db = DatabaseFactory.from(databasePath);
            if (db == null){
                return false;
            } else {
                getCurrentRequest().getWrappedSession().setAttribute(CURRENT_DATABASE_ATTRIBUTE_KEY, db);
                return true;
            }
        }
    }

    private static VaadinRequest getCurrentRequest() {
        VaadinRequest request = VaadinService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "No request bound to current thread.");
        }
        return request;
    }
}
