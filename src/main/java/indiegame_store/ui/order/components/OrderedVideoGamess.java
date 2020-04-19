package indiegame_store.ui.order.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.shared.Registration;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.GameType;
import indiegame_store.backend.domain.RentOrder;
import indiegame_store.ui.database.CurrentDatabase;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderedVideoGamess extends VerticalLayout implements HasValue<AbstractField.ComponentValueChangeEvent<OrderedVideoGamess, List<RentOrder.Item>>, List<RentOrder.Item>> {

    private boolean readOnly = false;

    private ComboBox<Game> gameComboBox;
    private TextField numberOfDays;
    private Grid<RentOrder.Item> gameToOrderGrid;
    private List<RentOrder.Item> gameToOrder = new ArrayList<>();
    private Button addButton;
    private Binder<RentOrder.Item> addFormBinder = new Binder<>(RentOrder.Item.class);

    public OrderedVideoGamess() {
        setId("ordered-videos");

        setMargin(false);
        setSpacing(false);

        cleanForm();

        gameComboBox = new ComboBox<>("Game to order");
        gameComboBox.setId("game-to-order");
        gameComboBox.setWidth("100%");
        gameComboBox.setRequired(true);
        gameComboBox.setItemLabelGenerator(Game::getName);
        // TODO: List only available games
        gameComboBox.setItems(CurrentDatabase.get().getGameTable().getAll());
        addFormBinder.forField(gameComboBox)
                .asRequired()
                .bind("game");
        add(gameComboBox);

        numberOfDays = new TextField("Number of days");
        numberOfDays.setId("number-of-days");
        numberOfDays.setWidth("100%");
        numberOfDays.setRequired(true);
        addFormBinder.forField(numberOfDays)
                .asRequired()
                .withConverter(new StringToIntegerConverter("Invalid number of days"))
                // TODO: Validation here. Number of days should be more than zero.
                .bind("days");

        add(numberOfDays);

        addButton = new Button("Add to order");
        addButton.setId("add-to-order-button");
        addButton.setWidth("100%");
        addButton.addClickListener(event -> {
            if (!addFormBinder.validate().isOk()) {
                return;
            }

            RentOrder.Item itemToAdd = new RentOrder.Item();
            itemToAdd.setGame(addFormBinder.getBean().getGame());
            itemToAdd.setGameType(addFormBinder.getBean().getGame().getGameType());
            itemToAdd.setDays(addFormBinder.getBean().getDays());

            gameToOrder.add(itemToAdd);
            setValue(gameToOrder);

            cleanForm();
        });

        add(addButton);

        gameToOrderGrid = new Grid<>();
        gameToOrderGrid.setId("games-to-rent-table");
        gameToOrderGrid.setWidth("100%");
        gameToOrderGrid.setHeightByRows(true);
        gameToOrderGrid.addColumn(itemToOrder -> itemToOrder.getGame().getName())
                .setHeader("Games")
                .setId("game");
        gameToOrderGrid.addColumn(item -> item.getGameType().getTextualRepresentation())
                .setHeader("Type")
                .setId("type");
        gameToOrderGrid.addColumn(RentOrder.Item::getDays)
                .setHeader("Days")
                .setId("days");
        gameToOrderGrid.addColumn(new ComponentRenderer<>(item -> {
            Checkbox checkboxPaidWithBonus = new Checkbox();
            checkboxPaidWithBonus.setValue(item.isPaidByBonus());
            checkboxPaidWithBonus.addValueChangeListener(event -> item.setPaidByBonus(event.getValue()));

            if (isReadOnly() || !item.getGameType().equals(GameType.NEW)) {
                checkboxPaidWithBonus.setReadOnly(true);
            }

            return checkboxPaidWithBonus;
        }))
                .setHeader("Pay with bonus")
                .setId("paidByBonus");

        gameToOrderGrid.addColumn(new LocalDateRenderer<>(RentOrder.Item::getReturnedDay, DateTimeFormatter.ofPattern("dd-MM-yyyy"), "-"))
                .setHeader("Return date")
                .setKey("return-date")
                .setSortable(false)
                .setId("return-date");

        gameToOrderGrid.addColumn(
                TemplateRenderer.<RentOrder.Item>of("<iron-icon icon=\"vaadin:close\" on-click='handleClick' style='cursor: pointer;'></iron-icon>").withEventHandler("handleClick", item -> {
                    gameToOrder.remove(item);
                    setValue(gameToOrder);
                    gameToOrderGrid.getDataProvider().refreshAll();
                })
        )
                .setKey("remove")
                .setFlexGrow(0)
                .setSortable(false)
                .setId("remove");

        add(gameToOrderGrid);
    }

    private void cleanForm() {
        addFormBinder.setBean(new RentOrder.Item());
    }

    @Override
    public List<RentOrder.Item> getValue() {
        return gameToOrder;
    }

    @Override
    public void setValue(List<RentOrder.Item> value) {
        if (value == null) {
            gameToOrderGrid.setItems();
            gameToOrder = new ArrayList<>();
            return;
        }

        List<RentOrder.Item> oldValue = new ArrayList<>(value);

        gameToOrder = value;
        gameToOrderGrid.setItems(gameToOrder);

        ComponentUtil.fireEvent(this, new AbstractField.ComponentValueChangeEvent<>(this, this, oldValue, false));
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderedVideoGamess, List<RentOrder.Item>>> listener) {
        @SuppressWarnings("rawtypes")
        ComponentEventListener componentListener = event -> listener.valueChanged((AbstractField.ComponentValueChangeEvent<OrderedVideoGamess, List<RentOrder.Item>>) event);
        return ComponentUtil.addListener(this, AbstractField.ComponentValueChangeEvent.class, componentListener);
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        gameToOrderGrid.getColumnByKey("remove").setVisible(!readOnly);
        gameToOrderGrid.getColumnByKey("return-date").setVisible(readOnly);
        addButton.setVisible(!readOnly);
        gameComboBox.setVisible(!readOnly);
        numberOfDays.setVisible(!readOnly);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return true;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        // Ignored, aways true
    }
}
