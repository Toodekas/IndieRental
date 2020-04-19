package indiegame_store.ui.inventory.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import indiegame_store.backend.domain.Game;

import java.util.Comparator;

/**
 * Games grid
 */
public class GameGrid extends Grid<Game> {

    public GameGrid() {
        setId("data-grid");
        setSizeFull();

        addColumn(Game::getId)
                .setHeader("Game ID")
                .setFlexGrow(1)
                .setSortable(true)
                .setId("game-id");
        addColumn(Game::getName)
                .setHeader("Game name")
                .setFlexGrow(20)
                .setSortable(true)
                .setId("game-name");
        addColumn(item -> item.getGameType().getTextualRepresentation())
                .setHeader("Type")
                .setFlexGrow(5)
                .setId("game-type");
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availabilityClass]]\"></iron-icon> [[item.stockCount]]";
        addColumn(TemplateRenderer.<Game>of(availabilityTemplate)
                .withProperty("availabilityClass", game -> game.getStockCount() > 0 ? "Available" : "NotAvailable")
                .withProperty("stockCount", game -> game.getStockCount() == 0 ? "-" : Integer.toString(game.getStockCount())))
                .setHeader("Availability")
                .setComparator(Comparator.comparing(Game::getStockCount))
                .setFlexGrow(3)
                .setId("game-availability");
    }
}
