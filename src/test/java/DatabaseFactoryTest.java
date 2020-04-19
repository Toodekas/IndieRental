import org.junit.jupiter.api.Test;
import indiegame_store.backend.database.Database;
import indiegame_store.backend.database.DatabaseFactory;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.GameType;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseFactoryTest {

    @Test
    void nonExistingDatabase() {
        assertNull(DatabaseFactory.from("src/test/resources/missing.json"));
    }

    @Test
    void databaseGameTestJSON(){ // test to see if one element from the games is broken that it returns null
        assertNull(DatabaseFactory.from("src/test/resources/broken_id.json"));
        assertNull(DatabaseFactory.from("src/test/resources/broken_name.json"));
        assertNull(DatabaseFactory.from("src/test/resources/broken_stock.json"));
        assertNull(DatabaseFactory.from("src/test/resources/broken_type.json"));
    }

    @Test
    void conversionTestYAML(){
        assertNotNull(DatabaseFactory.from("src/test/resources/working_db.yaml"));
    }

    @Test
    void dataToDatabase(){
        Database db = DatabaseFactory.from("src/test/resources/working_db.json");
        Game game = new Game();
        game.setGameType(GameType.REGULAR);
        game.setType(2);
        game.setStockCount(12);
        game.setName("Test game");
        db.getGameTable().createOrUpdate(game);
        db.updateDB();

        Database db_updated = DatabaseFactory.from("src/test/resources/working_db.json");
        Game newGame = db_updated.getGameTable().findById(game.getId());
        assertTrue(newGame.getId() == (game.getId()));
    }

}