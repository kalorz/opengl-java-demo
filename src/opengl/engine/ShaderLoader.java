package opengl.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ShaderLoader implements ResourceLoader<String> {

    @Override
    public String load(String path) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(Config.SHADERS_PATH + path);

            if (inputStream == null) {
                throw new IOException("Resource \"" + path + "\" not found");
            }

            StringBuffer buffer = new StringBuffer();
            Scanner scanner = new Scanner(inputStream);

            try {
                while (scanner.hasNextLine()) {
                    buffer.append(scanner.nextLine() + "\n");
                }
            } finally {
                scanner.close();
            }

            return buffer.toString();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
