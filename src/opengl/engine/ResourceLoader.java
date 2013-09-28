package opengl.engine;

import java.io.IOException;

public interface ResourceLoader<ResourceClass> {

    public ResourceClass load(String path) throws IOException;

}
