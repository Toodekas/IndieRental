package indiegame_store.backend.domain;

/**
 * Game domain object
 */
public class Game extends DBObject<Game> {

    /**
     * Game name
     */
    private String name;

    /**
     * Games in stock
     */
    private int stockCount = -1;

    /**
     * Game type
     */
    private GameType gameType = GameType.NEW;
    private int type = -1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void setter(Game game) {
        setName(game.getName());
        setStockCount(game.getStockCount());
        setGameType(game.getGameType());
    }

    @Override
    public boolean isValid() {
        boolean b = super.isValid() && gameType != null && stockCount != -1 && name != null;
        return b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        type = gameType.getDatabaseId();
    }

    public void fromJson() {
        if(getType() == 1){
            setGameType(GameType.NEW);

        }
        else if(getType() == 2){
            setGameType(GameType.REGULAR);

        }
        else if(getType() == 3){
            setGameType(GameType.OLD);
        }

    }
}
