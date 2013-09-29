package opengl.util;

import javax.media.opengl.GL2;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

public class GLUtils {

    public static void setupContext(GL2 gl) {
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_ALPHA_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        //gl.glEnable(GL2.GL_COLOR_MATERIAL);
        //gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);
        gl.glHint(GL2.GL_GENERATE_MIPMAP_HINT, GL2.GL_NICEST);
        gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
        gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST);
    }

    public static void setupLight(GL2 gl, int light, float[][] lights) {
        float[] ambient  = {lights[0][0], lights[0][1], lights[0][2], lights[0][3]};
        float[] diffuse  = {lights[1][0], lights[1][1], lights[1][2], lights[1][3]};
        float[] specular = {lights[2][0], lights[2][1], lights[2][2], lights[2][3]};
        float[] position = {lights[3][0], lights[3][1], lights[3][2], lights[3][3]};

        gl.glLightfv(light, GL2.GL_AMBIENT,  ambient,  0);
        gl.glLightfv(light, GL2.GL_DIFFUSE,  diffuse,  0);
        gl.glLightfv(light, GL2.GL_SPECULAR, specular, 0);
        gl.glLightfv(light, GL2.GL_POSITION, position, 0);
    }

    public static void setupLights(GL2 gl, int firstLight, float[][][] lights) {
        for (int i = 0; i < lights.length; i++) {
            setupLight(gl, firstLight + i, lights[i]);
        }
    }

    public static void toggleGouraud(GL2 gl, boolean enabled) {
        gl.glShadeModel(enabled ? GL2.GL_SMOOTH : GL2.GL_FLAT);
    }

    public static void toggleShader(GL2 gl, int program, boolean enabled) {
        gl.glUseProgram(enabled ? program : 0);
    }

    public static int compileShaderProgram(GL2 gl, int[] shaders) {
        int shaderProgram = gl.glCreateProgram();

        if (shaderProgram < 1) {
            System.err.println("Could not create Shader Program");
        } else {
            for (int shader : shaders) {
                gl.glAttachShader(shaderProgram, shader);
            }

            gl.glLinkProgram(shaderProgram);
            gl.glValidateProgram(shaderProgram);

            String status = GLUtils.getProgramStatus(gl, shaderProgram, GL2.GL_LINK_STATUS);
            if (status != null) {
                System.err.println("Shader linking error:");
                System.err.println(status);

                shaderProgram = 0;
            }
        }

        return shaderProgram;
    }

    public static void toggleLighting(GL2 gl, boolean enabled) {
        if (enabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        } else {
            gl.glDisable(GL2.GL_LIGHTING);
        }
    }

    public static void toggleLight(GL2 gl, int light, boolean enabled) {
        if (enabled) {
            gl.glEnable(light);
        } else {
            gl.glDisable(light);
        }
    }

    public static void toggleLights(GL2 gl, int firstLight, boolean[] lights) {
        for (int i = 0; i < lights.length; i++) {
            toggleLight(gl, firstLight + i, lights[i]);
        }
    }

    public static int compileShader(GL2 gl, int type, String source) {
        int shader = gl.glCreateShader(type);

        gl.glShaderSource(shader, 1, new String[] { source }, (int[]) null, 0);
        gl.glCompileShader(shader);

        return shader;
    }

    public static String getProgramStatus(GL2 gl, int program, int status) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(program, status, intBuffer);

        if (intBuffer.get(0) != 1) {
            gl.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, intBuffer);

            int size = intBuffer.get(0);
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(program, size, intBuffer, byteBuffer);
                return new String(byteBuffer.array(), Charset.forName("ASCII"));
            } else {
                return "Unknown";
            }
        }

        return null;
    }
}
