package indiegame_store.ui.inventory;

import com.vaadin.flow.component.UI;
import indiegame_store.ui.database.CurrentDatabase;
import indiegame_store.backend.database.DBTableRepository;
import indiegame_store.backend.domain.Game;

public class VGameStoreInventoryLogic {

    private VGameStoreInventory view;

    private DBTableRepository<Game> gameDBTableRepository;

    public VGameStoreInventoryLogic(VGameStoreInventory gamerStoreInventory) {
        view = gamerStoreInventory;
    }

    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }
        gameDBTableRepository = CurrentDatabase.get().getGameTable();

        view.setNewGameEnabled(true);
        view.setGames(gameDBTableRepository.getAll());
    }

    public void cancelGame() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String gameId) {
        String fragmentParameter;
        if (gameId == null || gameId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = gameId;
        }

        UI.getCurrent().navigate(VGameStoreInventory.class, fragmentParameter);
    }

    public void enter(String gameId) {
        if (gameId != null && !gameId.isEmpty()) {
            if (gameId.equals("new")) {
                newGame();
            } else {
                try {
                    int pid = Integer.parseInt(gameId);
                    Game game = findGame(pid);
                    view.selectRow(game);
                } catch (NumberFormatException ex) {
                    // Ignored
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Game findGame(int gameId) {
        return gameDBTableRepository.findById(gameId);
    }

    public void saveGame(Game game) {
        boolean isNew = game.isNewObject();

        Game updatedGameObject = gameDBTableRepository.createOrUpdate(game);

        if (isNew) {
            view.addGame(updatedGameObject);
        } else {
            view.updateGame(game);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(game.getName() + (isNew ? " created" : " updated"));
        CurrentDatabase.get().updateDB();
    }

    public void deleteGame(Game game) {
        gameDBTableRepository.remove(game);

        view.clearSelection();
        view.removeGame(game);

        setFragmentParameter("");
        view.showSaveNotification(game.getName() + " removed");
        CurrentDatabase.get().updateDB();
    }

    /**
     * Method fired when user selects game which he want to edit.
     *
     * @param Game game  object
     */
    public void editGame(Game game) {
        if (game == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(game.getId() + "");
        }
        view.editGame(game);
        CurrentDatabase.get().updateDB();
    }

    public void newGame() {
        view.editGame(new Game());
        view.clearSelection();
        setFragmentParameter("new");
        CurrentDatabase.get().updateDB();
    }

    public void rowSelected(Game game) {
        editGame(game);
    }
}
