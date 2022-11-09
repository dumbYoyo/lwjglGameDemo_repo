package engine;

import engine.objects.Entity;

public interface IHud {
    Entity[] getGameObjects();

    default void cleanUp() {
        Entity[] entities = getGameObjects();
        for (Entity entity : entities) {
            entity.getEntityData().getMesh().cleanUp();
        }
    }
}
