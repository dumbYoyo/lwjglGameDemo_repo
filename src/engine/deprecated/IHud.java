package engine.deprecated;

import engine.objects.Entity;

@Deprecated
public interface IHud {
    Entity[] getGameObjects();

    default void cleanUp() {
        Entity[] entities = getGameObjects();
        for (Entity entity : entities) {
            entity.getEntityData().getMesh().cleanUp();
        }
    }
}
