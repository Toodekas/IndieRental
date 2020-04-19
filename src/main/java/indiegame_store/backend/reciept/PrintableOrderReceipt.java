package indiegame_store.backend.reciept;

import indiegame_store.backend.domain.GameType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Rent order receipt printer
 */
public class PrintableOrderReceipt implements PrintableReceipt {

    private String orderId;
    private LocalDate orderDate;
    private String customerName;
    private List<Item> orderItems;
    private BigDecimal totalPrice;
    private int remainingBonusPoints;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getRemainingBonusPoints() {
        return remainingBonusPoints;
    }

    public void setRemainingBonusPoints(int remainingBonusPoints) {
        this.remainingBonusPoints = remainingBonusPoints;
    }

    public String print() {
        StringBuilder receipt = new StringBuilder()
                .append("ID: ").append(getOrderId())
                .append("\n")
                //Formated rent date
                .append("Date: ").append(getOrderDate().format(DateTimeFormatter.ofPattern("dd-MM-YY")))
                .append("\n").append("Customer: ").append(getCustomerName())
                .append("\n");

        boolean paidAnyUsingBonus = false;

        for (PrintableOrderReceipt.Item orderItem : getOrderItems()) {
            receipt.append(orderItem.print());

            if (orderItem.getPaidBonus() != null) {
                paidAnyUsingBonus = true;
            }
        }

        receipt.append("\n");
        receipt.append("Total price: ").append(getTotalPrice()).append(" EUR");

        if (paidAnyUsingBonus) {
            receipt.append("\nRemaining Bonus points: ").append(getRemainingBonusPoints());
        }

        return receipt.toString();
    }

    public static class Item {

        private String gameName;
        private GameType gameType;
        private int days;
        private BigDecimal paidMoney = null;
        private Integer paidBonus = null;

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public GameType getGameType() {
            return gameType;
        }

        public void setGameType(GameType gameType) {
            this.gameType = gameType;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public BigDecimal getPaidMoney() {
            return paidMoney;
        }

        public void setPaidMoney(BigDecimal paidMoney) {
            this.paidMoney = paidMoney;
        }

        public Integer getPaidBonus() {
            return paidBonus;
        }

        public void setPaidBonus(Integer paidBonus) {
            this.paidBonus = paidBonus;
        }

        public String print() {
            StringBuilder receipt = new StringBuilder();
            receipt.append(getGameName())
                    .append(" (")
                    .append(getGameType().getTextualRepresentation())
                    .append(") ")
                    .append(getDays());

            //Prints days in plural or single form
            if (getDays() > 1){
                receipt.append(" days ");
            } else {
                receipt.append(" day ");
            }

            if (getPaidBonus() != null) {
                receipt.append("(Paid with ").append(getPaidBonus()).append(" Bonus points) ");
            } else {
                receipt.append(getPaidMoney()).append(" EUR");
            }

            receipt.append("\n");

            return receipt.toString();
        }
    }
}
