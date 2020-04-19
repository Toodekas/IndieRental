package indiegame_store.ui.order.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import indiegame_store.backend.database.DBTableRepository;
import indiegame_store.backend.domain.RentOrder;
import indiegame_store.backend.domain.ReturnOrder;
import indiegame_store.backend.reciept.OrderToReceiptService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ReturnGameWindow extends Dialog {

    private RentOrder currentOrder;
    private ReturnOrder returnOrder = new ReturnOrder();
    private OrderToReceiptService orderToReceiptService;
    private DBTableRepository<RentOrder> repository;

    private List<RentOrder.Item> items;

    private CheckboxGroup<RentOrder.Item> gameToReturnCheckBox;
    private Element printedReceipt;

    private CloseEvent closeEvent;

    public ReturnGameWindow(RentOrder currentOrder, OrderToReceiptService orderToReceiptService, DBTableRepository<RentOrder> repository, CloseEvent closeEvent) {
        this.currentOrder = currentOrder;
        this.orderToReceiptService = orderToReceiptService;
        this.repository = repository;
        this.closeEvent = closeEvent;

        this.items = currentOrder.getItems().stream()
                .filter(item -> Objects.isNull(item.getReturnedDay()))
                .collect(Collectors.toList());

        buildLayout();
        updatePrintedReceipt();
    }

    private void buildLayout() {
        setId("return-games-window");
        setWidth("600px");
        setHeight("600px");

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        returnOrder.setRentOrder(currentOrder);
        returnOrder.setReturnDate(LocalDate.now());

        printedReceipt = ElementFactory.createPreformatted();
        printedReceipt.setProperty("id", "receipt-content");

        Div printedReceiptDiv = new Div();
        printedReceiptDiv.getElement().appendChild(printedReceipt);
        printedReceiptDiv.setWidth("100%");

        layout.add(printedReceiptDiv);
        layout.setFlexGrow(1, printedReceiptDiv);

        gameToReturnCheckBox = new CheckboxGroup<>();
        gameToReturnCheckBox.setId("games-to-return-combobox");
        gameToReturnCheckBox.setLabel("Select games to return");
        gameToReturnCheckBox.setItems(items);
        gameToReturnCheckBox.setItemLabelGenerator(item -> item.getGame().getName());
        gameToReturnCheckBox.addValueChangeListener(event -> {
            returnOrder.setItems(new ArrayList<>(event.getValue()));
            updatePrintedReceipt();
        });

        layout.add(gameToReturnCheckBox);
        layout.add(getButtonLayout());

        add(layout);
    }

    private void updatePrintedReceipt() {
        printedReceipt.setText(orderToReceiptService.convertRentOrderToReceipt(returnOrder).print());
    }

    private HorizontalLayout getButtonLayout() {
        HorizontalLayout buttonGroup = new HorizontalLayout();
        Button cancel = new Button("Cancel");
        cancel.setId("cancel-return");
        cancel.addClickListener(event -> close());

        Button save = new Button("Approve and Save");
        save.setId("return-games");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            save();
            close();
        });

        buttonGroup.add(cancel, save);

        return buttonGroup;
    }

    private void save() {
        Set<RentOrder.Item> returnedItems = gameToReturnCheckBox.getValue();

        for (RentOrder.Item rentedItem : currentOrder.getItems()) {
            returnedItems.stream()
                    .filter(returnedItem -> returnedItem.getGame().equals(rentedItem.getGame()))
                    .findAny()
                    .ifPresent(gamesToReturn -> gamesToReturn.setReturnedDay(returnOrder.getReturnDate()));
        }

        repository.createOrUpdate(currentOrder);
    }

    @Override
    public void close() {
        super.close();

        if (closeEvent != null) {
            closeEvent.closed();
        }
    }

    @FunctionalInterface
    public interface CloseEvent {
        void closed();
    }
}
