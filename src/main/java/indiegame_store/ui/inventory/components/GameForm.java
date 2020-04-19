package indiegame_store.ui.inventory.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import indiegame_store.ui.inventory.VGameStoreInventoryLogic;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.GameType;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Game form
 */
public class GameForm extends Div {

    private VerticalLayout content;

    private TextField name;
    private TextField stockCount;
    private ComboBox<GameType> type;
    private Button save;
    private Button cancel;
    private Button delete;

    private VGameStoreInventoryLogic viewLogic;
    private Binder<Game> binder;
    private Game currentGame;

    public GameForm(VGameStoreInventoryLogic gameRentalInventoryLogic) {
        setId("edit-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = gameRentalInventoryLogic;

        name = new TextField("Game name");
        name.setId("game-name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        type = new ComboBox<>("Game type");
        type.setId("game-type");
        type.setWidth("100%");
        type.setRequired(true);
        type.setItems(GameType.values());
        type.setItemLabelGenerator(GameType::getTextualRepresentation);
        content.add(type);

        stockCount = new TextField("In stock");
        stockCount.setId("stock-count");
        stockCount.setWidth("100%");
        stockCount.setRequired(true);
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(stockCount);

        // Binding field to domain
        binder = new Binder<>(Game.class);
        binder.forField(name)
                .bind("name");
        binder.forField(type)
                .bind("gameType");
        binder.forField(stockCount).withConverter(new StockCountConverter())
                .bind("stockCount");

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
        });

        save = new Button("Save");
        save.setId("save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentGame != null) {
                // TODO: Validation for game name, validate that game type is selected
                binder.writeBeanIfValid(currentGame);
                viewLogic.saveGame(currentGame);
            }
        });

        cancel = new Button("Cancel");
        cancel.setId("cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelGame());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelGame())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setId("delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentGame != null) {
                viewLogic.deleteGame(currentGame);
            }
        });

        content.add(save, delete, cancel);
    }

    public void editGame(Game game) {
        if (game == null) {
            game = new Game();
        }

        
        currentGame = game;
        binder.readBean(game);

        System.out.println(game);
        // TODO: Delete game button should be inactive if it’s new game creation or it was rented at least one time.
        if (game.isNewObject()) {
            delete.setEnabled(false);
        } else {
            delete.setEnabled(true);
        }
    }

    private static class StockCountConverter extends StringToIntegerConverter {

        public StockCountConverter() {
            super(0, "Could not convert value to " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }
}
