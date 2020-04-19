package indiegame_store.backend.reciept;

import indiegame_store.backend.domain.GameType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Return receipt printer
 */
public class PrintableReturnReceipt implements PrintableReceipt {

    private String orderId;
    private String customerName;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private BigDecimal totalCharge;
    private List<Item> returnedItems;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public void setRentDate(LocalDate rentDate) {
        this.rentDate = rentDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public List<Item> getReturnedItems() {
        return returnedItems;
    }

    public void setReturnedItems(List<Item> returnedItems) {
        this.returnedItems = returnedItems;
    }

    @Override
    public String print() {
        StringBuilder receipt = new StringBuilder()
                .append("ID: ").append(getOrderId()).append(" (Return)")
                .append("\n")
                .append("Rent date: ").append(getRentDate().format(DateTimeFormatter.ofPattern("dd-MM-YY")))
                .append("\n").append("Customer: ").append(getCustomerName())
                .append("\nReturn date: ").append(getReturnDate().format(DateTimeFormatter.ofPattern("dd-MM-YY")))
                .append("\n");

        returnedItems.forEach(item -> receipt.append(item.print()));

        receipt.append("\n");
        receipt.append("Total late change: ").append(getTotalCharge()).append(" EUR");

        return receipt.toString();
    }

    public static class Item implements PrintableReceipt {
        private String gameName;
        private GameType gameType;
        private int extraDays;
        private BigDecimal extraPrice;

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

        public int getExtraDays() {
            return extraDays;
        }

        public void setExtraDays(int extraDays) {
            this.extraDays = extraDays;
        }

        public BigDecimal getExtraPrice() {
            return extraPrice;
        }

        public void setExtraPrice(BigDecimal extraPrice) {
            this.extraPrice = extraPrice;
        }

        @Override
        public String print() {
            return getGameName()
                    .concat(" (")
                    .concat(getGameType().getTextualRepresentation())
                    .concat(") ")
                    .concat(Integer.toString(getExtraDays()))
                    .concat(" extra days ")
                    .concat(getExtraPrice().toString())
                    .concat(" EUR\n");
        }
    }
}
