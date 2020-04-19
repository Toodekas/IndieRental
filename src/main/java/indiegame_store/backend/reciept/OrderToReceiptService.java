package indiegame_store.backend.reciept;

import indiegame_store.backend.domain.GameType;
import indiegame_store.backend.domain.RentOrder;
import indiegame_store.backend.domain.ReturnOrder;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple receipt creation service
 * <p>
 * Note! All calculations should be in another place. Here we just setting already calculated data. Feel free to refactor.
 */
public class OrderToReceiptService {

    /**
     * Converts rent order to printable receipt
     *
     * @param order rent object
     * @return Printable receipt object
     */
    public PrintableOrderReceipt convertRentOrderToReceipt(RentOrder order) {
        PrintableOrderReceipt printableOrderReceipt = new PrintableOrderReceipt();

        printableOrderReceipt.setOrderId(order.isNewObject() ? "new" : Integer.toString(order.getId()));
        printableOrderReceipt.setOrderDate(order.getOrderDate());
        printableOrderReceipt.setCustomerName(order.getCustomer().getName());

        List<PrintableOrderReceipt.Item> itemList = new ArrayList<>();
        printableOrderReceipt.setOrderItems(itemList);

        int spentBonusPoints = 0;
        BigDecimal totalPaidMoney = BigDecimal.ZERO;

        for (RentOrder.Item orderItem : order.getItems()) {
            PrintableOrderReceipt.Item item = new PrintableOrderReceipt.Item();
            item.setDays(orderItem.getDays());
            item.setGameName(orderItem.getGame().getName());
            item.setGameType(orderItem.getGameType());
            //Calculated data adding
            if (orderItem.isPaidByBonus() && orderItem.getGameType() == GameType.NEW) {
                item.setPaidBonus(25 * orderItem.getDays());
                spentBonusPoints = spentBonusPoints +  item.getPaidBonus();
            } else {
                item.setPaidMoney(calculateCharge(orderItem.getDays(), orderItem.getGameType()));
                totalPaidMoney = totalPaidMoney.add(calculateCharge(orderItem.getDays(), orderItem.getGameType()));
            }

            itemList.add(item);
        }

        printableOrderReceipt.setTotalPrice(totalPaidMoney);

        printableOrderReceipt.setRemainingBonusPoints(order.getCustomer().getPoints() - spentBonusPoints);

        return printableOrderReceipt;
    }

    /**
     * Converts return order to printable receipt
     *
     * @param order return object
     * @return Printable receipt object
     */
    public PrintableReturnReceipt convertRentOrderToReceipt(ReturnOrder order) {
        PrintableReturnReceipt receipt = new PrintableReturnReceipt();

        receipt.setOrderId(Integer.toString(order.getRentOrder().getId()));
        receipt.setCustomerName(order.getRentOrder().getCustomer().getName());
        receipt.setRentDate(order.getRentOrder().getOrderDate());
        receipt.setReturnDate(order.getReturnDate());

        List<PrintableReturnReceipt.Item> returnedItems = new ArrayList<>();
        BigDecimal totalExtraCharge = BigDecimal.ZERO;
        if (order.getItems() != null) {
            for (RentOrder.Item rentedItem : order.getItems()) {
                PrintableReturnReceipt.Item item = new PrintableReturnReceipt.Item();
                item.setGameName(rentedItem.getGame().getName());
                item.setGameType(rentedItem.getGameType());
                //Calculated data how much later rented game was returned
                int itemDays = ((int) ChronoUnit.DAYS.between(order.getRentOrder().getOrderDate(), order.getReturnDate())) + 1;

                item.setExtraDays(Math.max(0, itemDays - rentedItem.getDays()));
                //Calculated data how much it costs for extra days
                item.setExtraPrice(calculateCharge(itemDays, rentedItem.getGameType()).subtract(calculateCharge(rentedItem.getDays(), rentedItem.getGameType())).max(BigDecimal.ZERO));
                totalExtraCharge = totalExtraCharge.add(item.getExtraPrice());
                returnedItems.add(item);
            }
        }
        receipt.setReturnedItems(returnedItems);

        receipt.setTotalCharge(totalExtraCharge);

        return receipt;
    }

    public static BigDecimal calculateCharge(int totalDays, GameType gameType){
        BigDecimal charge = BigDecimal.ZERO;
        if(gameType == GameType.NEW){
            charge = new BigDecimal(4).multiply(new BigDecimal(totalDays));
        }
        else if(gameType == GameType.REGULAR){
            charge = (new BigDecimal(3)).add(new BigDecimal(Math.max(0, totalDays - 3) * 3));
        }
        else if(gameType == GameType.OLD){
            charge = (new BigDecimal(3)).add(new BigDecimal(Math.max(0, totalDays - 5) * 3));
        }
        return charge;
    }

}
