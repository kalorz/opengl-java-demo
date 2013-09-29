package opengl.resources;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import opengl.engine.Config;

import javax.media.opengl.GLException;
import java.io.IOException;
import java.io.InputStream;

public class TextureLoader implements ResourceLoader<Texture> {

    @Override
    public Texture load(String path) throws IOException {
        Texture texture = null;
        InputStream textureStream = null;

        try {
            textureStream = this.getClass().getClassLoader().getResourceAsStream(Config.TEXTURES_PATH + path);

            if (textureStream == null) {
                throw new IOException("Resource \"" + path + "\" not found");
            }

            texture = TextureIO.newTexture(textureStream, true, IOUtil.getFileSuffix(path));

            System.out.println("Loaded Texture: " + path + " (" + texture.getImageWidth() + "x" + texture.getImageHeight() + ", " + texture.getEstimatedMemorySize() / 1024 + " kB)");
        } catch (GLException ex) {
            throw new IOException(ex);
        } finally {
            if (textureStream != null) {
                textureStream.close();
            }
        }

        return texture;
    }

}
