package engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Shader {
    private String vertexPath, fragmentPath, vertexShaderSource, fragmentShaderSource;
    private int vertexID, fragmentID, programID;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexPath = vertexShaderPath;
        this.fragmentPath = fragmentShaderPath;
        this.vertexShaderSource = loadShaderSource(vertexPath);
        this.fragmentShaderSource = loadShaderSource(fragmentPath);
        this.programID = GL20.glCreateProgram();

        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexID, vertexShaderSource);

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentID, fragmentShaderSource);
    }

    public void compile() {
        compileShader(vertexID, "vertex");
        compileShader(fragmentID, "fragment");

        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID); // when releasing, remove this line of code

        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        unbind();
        GL20.glDeleteProgram(programID);
    }

    public void loadMatrix4f(String uniformName, Matrix4f data) {
        bind();
        int location = GL20.glGetUniformLocation(programID, uniformName);

        /// TIP: never allocate stuff like this, it never gets freed ///
        //FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        //data.get(buffer);

        /// do this instead ///
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(location, false, data.get(stack.mallocFloat(16)));
        }
        // stuff gets freed
    }

    public void loadFloat(String uniformName, float data) {
        bind();
        int location = GL20.glGetUniformLocation(programID, uniformName);
        GL20.glUniform1f(location, data);
    }

    public void loadInteger(String uniformName, int data) {
        bind();
        int location = GL20.glGetUniformLocation(programID, uniformName);
        GL20.glUniform1i(location, data);
    }

    public void loadVec3f(String uniformName, Vector3f data) {
        bind();
        int location = GL20.glGetUniformLocation(programID, uniformName);
        GL20.glUniform3f(location, data.x, data.y, data.z);
    }

    public void loadVec4f(String uniformName, Vector4f data) {
        bind();
        int location = GL20.glGetUniformLocation(programID, uniformName);
        GL20.glUniform4f(location, data.x, data.y, data.z, data.w);
    }

    private void compileShader(int shader, String type) {
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            if (type == "fragment" || type == "Fragment") {
                System.err.println("ERROR: " + GL20.glGetProgramInfoLog(programID, GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH)) + " at: " + fragmentPath);
            } else if (type == "vertex" || type == "Vertex") {
                System.err.println("ERROR: " + GL20.glGetProgramInfoLog(programID, GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH)) + " at: " + vertexPath);
            }
            System.exit(-1);
        }
    }

    private String loadShaderSource(String shaderPath) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(shaderPath))) {
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot read shader at: " + shaderPath);
        }

        return stringBuilder.toString();
    }
}
