package indiegame_store.ui.order.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import indiegame_store.backend.domain.RentOrder;

public class OrderGrid extends Grid<RentOrder> {

    public OrderGrid() {
        setSizeFull();
        setId("data-grid");

        addColumn(RentOrder::getId)
                .setSortable(true)
                .setHeader("Order ID")
                .setId("order-id");
        addColumn(order -> order.getCustomer().getName())
                .setFlexGrow(20)
                .setSortable(true)
                .setHeader("Customer name")
                .setId("customer-name");
        addColumn(new LocalDateRenderer<>(RentOrder::getOrderDate, "dd-MM-yyyy"))
                .setFlexGrow(5)
                .setSortable(true)
                .setHeader("Order date")
                .setId("order-date");
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.statusClass]]\"></iron-icon>";
        addColumn(TemplateRenderer.<RentOrder>of(availabilityTemplate)
                .withProperty("statusClass", order -> {
                    // TODO: Implement flagging system using rules below:
                    // Return "Ok" if there is no any overdue, if all games are returned
                    // Return "Horrible" if all games were not returned in time
                    // Return "SoSo" if ablest one game was not returned in time
                    return "Ok";
                }))
                .setHeader("Status")
                .setFlexGrow(3)
                .setId("availability");
    }
}
