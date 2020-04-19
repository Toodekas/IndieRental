package indiegame_store.backend.database;

import indiegame_store.backend.domain.DBObject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Database repository interface
 *
 * @param <T> Stored data type
 */
public class DBTableRepository<T extends DBObject> {
    private List<T> objectList;

    public DBTableRepository(List<T> objectList) {
        this.objectList = objectList;
        Collections.sort(objectList);
    }

    /**
     * Fetches all object from table
     *
     * @return list of object
     */
    public List<T> getAll(){
        return objectList;
    }

    /**
     * Finds specific object from table using ID field
     *
     * @param id object id
     * @return found object
     */
    public T findById(int id){
        Optional<T> first = objectList.stream().filter(game -> game.getId() == id).findFirst();
        if (!first.isPresent()){
            return null;
        }
        return first.get();
    }

    /**
     * Removes object
     *
     * @param object object for removal
     * @return object removed or not
     */
    public boolean remove(T object){
        return objectList.remove(object);
    }

    /**
     * Creates or updates object.
     * <p>
     * If object without ID or ID is -1, then it will be object creation. In case of creation ID should be set to provided object
     * If updating existing object, then returning object which was updated from database
     *
     * @param object object to create or update
     * @return updated object
     */
    public T createOrUpdate(T object){
        if (object == null) {
            return null;
        }

        if (object.isNewObject()) {
            object.setId(generateNextId());
            objectList.add(object);
            return object;
        }

        T databaseObject = findById(object.getId());

        databaseObject.setter(object);

        return databaseObject;
    }

    /**
     * New ID generation for table. Should be always unique
     *
     * @return next id sequence
     */
    public int generateNextId(){
        return objectList.get(objectList.size() - 1).getId() + 1;
    }

}
