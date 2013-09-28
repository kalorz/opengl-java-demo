package opengl.engine;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResourceRepository<ResourceClass> {
    private Map<String, ResourceClass> resources;
    private ResourceLoader<ResourceClass> loader;

    public ResourceRepository(ResourceLoader<ResourceClass> loader) {
        this.loader = loader;
    }

    public ResourceClass get(String key) throws IOException {
        if (!getResources().containsKey(key)) {
            getResources().put(key, loader.load(key));
        }

        return getResources().get(key);
    }

    public Map<String, ResourceClass> getResources() {
        if (resources == null) {
            resources = new HashMap<String, ResourceClass>();
        }

        return resources;
    }

    public Collection<ResourceClass> values() {
        return getResources().values();
    }

}
