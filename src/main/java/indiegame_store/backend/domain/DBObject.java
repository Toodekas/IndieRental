package indiegame_store.backend.domain;

public abstract class DBObject<T extends DBObject> implements Comparable<DBObject>{
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isValid(){
        if(id != -1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isNewObject() {
        if(id == -1){
            return true;
        }
        else{
            return false;
        }
    }

    abstract public <B extends T> void setter(B o);


    @Override
    public int compareTo(DBObject o) {
        return id - o.getId();
    }
}
