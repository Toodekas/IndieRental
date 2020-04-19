package indiegame_store.backend.domain;

/**
 * Game type
 * According that game rent price should be calculated
 */
public enum GameType {

    NEW(1, "New release"),
    REGULAR(2, "Regular rental"),
    OLD(3, "Old film");

    /**
     * Game type representation in database
     */
    private final int databaseId;

    /**
     * Textural representation in database
     */
    private final String textualRepresentation;

    GameType(int databaseId, String textualRepresentation) {
        this.databaseId = databaseId;
        this.textualRepresentation = textualRepresentation;
    }

    public String getTextualRepresentation() {
        return textualRepresentation;
    }

    public int getDatabaseId() {
        return databaseId;
    }
}
