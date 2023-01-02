package engine.utility;

import engine.objects.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityManager {
    private Map<String, Entity> entities_name = new HashMap<>();
    private Map<Integer, Entity> entities_Id = new HashMap<>();

    /**
     * Adds entity to both hashmaps namely entities_name and entities_Id.
     * Must be called when the name and id have been set for the entity.
     * @param entity
     */
    public void addEntity(Entity entity) {
        entities_name.put(entity.getName(), entity);
        entities_Id.put(entity.getId(), entity);
    }

    public Entity getEntity(String name) {
        return entities_name.get(name);
    }

    public Entity getEntity(int id) {
        return entities_Id.get(id);
    }

    public Map<String, Entity> getEntities_name() {
        return entities_name;
    }

    public Map<Integer, Entity> getEntities_Id() {
        return entities_Id;
    }
}
