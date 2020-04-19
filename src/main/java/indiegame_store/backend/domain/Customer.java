package indiegame_store.backend.domain;

/**
 * Customer domain object
 */
public class Customer extends DBObject<Customer>{

    /**
     * Customer name
     */
    private String name;

    /**
     * Customer bonus points
     */
    private int points = -1;


    @Override
    public void setter(Customer object) {
        setName(object.getName());
        setPoints(object.getPoints());
    }

    @Override
    public boolean isValid() {
        return super.isValid() && name != null && points != -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


}