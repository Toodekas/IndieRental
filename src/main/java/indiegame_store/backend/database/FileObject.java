package indiegame_store.backend.database;

import indiegame_store.backend.domain.Customer;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.RentOrder;

public class FileObject {
    private Game[] game;
    private Customer[] customer;
    private RentOrder[] order;

    public FileObject() {
        this.game = new Game[0];
        this.customer = new Customer[0];
        this.order = new RentOrder[0];
    }

    public Game[] getGames() {
        return game;
    }

    public void setGames(Game[] games) {
        this.game = games;
    }

    public Customer[] getCustomers() {
        return customer;
    }

    public void setCustomers(Customer[] customers) {
        this.customer = customers;
    }

    public RentOrder[] getRentOrders() {
        return order;
    }

    public void setRentOrders(RentOrder[] rentOrders) {
        this.order = rentOrders;
    }
}
