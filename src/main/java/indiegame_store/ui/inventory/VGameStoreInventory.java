package indiegame_store.ui.inventory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import indiegame_store.backend.domain.Game;
import indiegame_store.ui.MainLayout;
import indiegame_store.ui.inventory.components.GameForm;
import indiegame_store.ui.inventory.components.GameGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = VGameStoreInventory.VIEW_NAME, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class VGameStoreInventory extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Inventory";
    private GameGrid grid;
    private GameForm form;
    private TextField filter;

    private ListDataProvider<Game> dataProvider = new ListDataProvider<>(new ArrayList<>());
    private VGameStoreInventoryLogic viewLogic = new VGameStoreInventoryLogic(this);
    private Button newGame;

    public VGameStoreInventory() {
        setId(VIEW_NAME);
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new GameGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);

        form = new GameForm(viewLogic);

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setId("filter");
        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            // TODO: Implement filtering by game name
        });

        newGame = new Button("New Game");
        newGame.setId("new-item");
        newGame.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newGame.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newGame.addClickListener(click -> viewLogic.newGame());

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newGame);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewGameEnabled(boolean enabled) {
        newGame.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Game row) {
        grid.getSelectionModel().select(row);
    }

    public void addGame(Game game) {
        dataProvider.getItems().add(game);
        grid.getDataProvider().refreshAll();
    }

    public void updateGame(Game game) {
        dataProvider.refreshItem(game);
    }

    public void removeGame(Game game) {
        dataProvider.getItems().remove(game);
        dataProvider.refreshAll();
    }

    public void editGame(Game movie) {
        showForm(movie != null);
        form.editGame(movie);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
    }

    public void setGames(List<Game> movies) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(movies);
        dataProvider.refreshAll();
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}