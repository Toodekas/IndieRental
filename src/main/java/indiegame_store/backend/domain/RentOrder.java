package indiegame_store.backend.domain;

import indiegame_store.backend.database.DBTableRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * One rent by cust
 */
public class RentOrder extends DBObject<RentOrder>{
    /**
     * Customer
     */
    private Customer customerC;

    /**
     * Customer id
     */
    private int customer = -1;

    /**
     * Rent date
     */
    private LocalDate localDate = LocalDate.now();

    /**
     * Rent date as string
     */
    private String orderDate;

    /**
     * List of rented games
     */
    private List<Item> games = new ArrayList<>();

    private Item[] items;

    public Customer getCustomer() {
        return customerC;
    }

    public void setCustomer(Customer customer) {
        this.customerC = customer;
        this.customer = customer.getId();
    }

    public List<Item> getItems() {
        return games;
    }

    public void setItems(List<Item> items) {
        this.games = items;
        this.items = items.toArray(new Item[0]);
    }

    public LocalDate getOrderDate() {
        return localDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.localDate = orderDate;
        this.orderDate = orderDate.format(DateTimeFormatter.ISO_DATE);
    }
    public boolean validItems(){
        boolean validation = true;
        for(Item i : games){
            if(i.isValid() == false){
                validation = false;
            }
        }
        return validation;
    }

    public void fromJson(DBTableRepository<Customer> customerDBTableRepository) {
        if(orderDate != null) {
            LocalDate.parse(orderDate);
        }
        else{
            localDate = null;
        }
        customerC = customerDBTableRepository.findById(customer);
        games = Arrays.asList(items);
    }

    @Override
    public void setter(RentOrder rentOrder) {
        setCustomer(rentOrder.getCustomer());
        setOrderDate(rentOrder.getOrderDate());
        setItems(rentOrder.getItems());
    }
    @Override
    public boolean isValid() {
        return super.isValid() && (validItems() == true) && localDate != null && customerC != null;
    }

    /**
     * Rented gameObj entry
     */
    public static class Item {
        /**
         * Selected gameObj
         */
        private Game gameObject;

        private int game = -1;

        /**
         * Game type on a moment of renting
         */
        private GameType gameType;

        private int type = -1;

        /**
         * Number of renting days
         */
        private int days = -1;

        /**
         * Paid by bonus points
         */
        private boolean paidByBonus;

        /**
         * Return date. NULL if not returned yet.
         */
        private LocalDate returnedDate;

        private String returnedDay;

        public Game getGame() {
            return gameObject;
        }

        public void setGame(Game game) {
            this.gameObject = game;
            this.game = game.getId();
        }

        public GameType getGameType() {
            return gameType;
        }

        public void setGameType(GameType gameType) {
            this.gameType = gameType;
            this.type = gameType.getDatabaseId();
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public LocalDate getReturnedDay() {
            return returnedDate;
        }

        public void setReturnedDay(LocalDate returnedDay) {
            this.returnedDate = returnedDay;
        }

        public boolean isPaidByBonus() {
            return paidByBonus;
        }


        public void setPaidByBonus(boolean paidByBonus) {
            this.paidByBonus = paidByBonus;
        }
        

        public void fromJson(DBTableRepository<Game> gameDBObject) {
            gameObject = gameDBObject.findById(game);
            if(gameObject == null){
                gameType = null;
            }
            else{
                gameType = gameObject.getGameType();
            }
            if(returnedDay == null){
                returnedDay = null;
            }
            else {
                returnedDate = LocalDate.parse(returnedDay);
            }
        }

        public boolean isValid(){
            if(gameObject == null || gameType == null || days == -1){
                return false;
            }
            else{
                return true;
            }
        }
    }
}