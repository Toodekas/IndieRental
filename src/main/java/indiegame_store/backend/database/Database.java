package indiegame_store.backend.database;

import indiegame_store.backend.domain.Customer;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.RentOrder;

/**
 * Database proxy interface for getting access to database tables
 */
public interface Database {

    /**
     * Interface for getting "game" table
     *
     * @return "game" table repository
     */
    DBTableRepository<Game> getGameTable();

    /**
     * Interface for getting "customer" table
     *
     * @return "customer" table repository
     */
    DBTableRepository<Customer> getCustomerTable();

    /**
     * Interface for getting "order" table
     *
     * @return "order" table repository
     */
    DBTableRepository<RentOrder> getOrderTable();

    /**
     * Writes the database back to file
     */

    void updateDB();

}
