package engine.math;

import engine.objects.Camera;
import engine.objects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class uMatrix {
    private Matrix4f modelViewMatrix, modelMatrix, modelOrthoMatrix;
    private Matrix4f projMatrix;
    private Matrix4f viewMatrix;

    private Matrix4f ortho;

    public uMatrix() {
        modelViewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        projMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelOrthoMatrix = new Matrix4f();
        ortho = new Matrix4f();
    }

    public Matrix4f getProjMatrix(float fov, float aspect, float near, float far) {
        projMatrix.identity();
        projMatrix.perspective(fov, aspect, near, far);
        return projMatrix;
    }

    public Matrix4f getOrthoMatrix(float left, float right, float bottom, float top) {
        ortho.identity();
        ortho.setOrtho2D(left, right, bottom, top);
        return ortho;
    }

    public Matrix4f getOrthoModelMatrix(GameObject gameObject, Matrix4f ortho) {
        modelMatrix.identity();
        modelMatrix.translate(gameObject.getPosition());
        modelMatrix.rotate(gameObject.getRotation().x, new Vector3f(1, 0, 0));
        modelMatrix.rotate(gameObject.getRotation().y, new Vector3f(0, 1, 0));
        modelMatrix.rotate(gameObject.getRotation().z, new Vector3f(0, 0, 1));
        modelMatrix.scale(gameObject.getScale());
        return ortho.mul(modelMatrix);
    }

    public Matrix4f getOrthoModelMatrix(float posX, float posY, Matrix4f ortho) {
        modelOrthoMatrix.identity();
        modelOrthoMatrix.translate(posX, posY, 0f);

        modelOrthoMatrix.scale(1);
        return ortho.mul(modelOrthoMatrix);
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        modelViewMatrix.identity();
        modelViewMatrix.translate(gameObject.getPosition());
        modelViewMatrix.rotate(gameObject.getRotation().x, new Vector3f(1, 0, 0));
        modelViewMatrix.rotate(gameObject.getRotation().y, new Vector3f(0, 1, 0));
        modelViewMatrix.rotate(gameObject.getRotation().z, new Vector3f(0, 0, 1));
        modelViewMatrix.scale(gameObject.getScale());
        return viewMatrix.mul(modelViewMatrix);
    }

    public Matrix4f getModelMatrix(GameObject gameObject) {
        modelMatrix.identity();
        modelMatrix.translate(gameObject.getPosition());
        modelMatrix.rotate(gameObject.getRotation().x, new Vector3f(1, 0, 0));
        modelMatrix.rotate(gameObject.getRotation().y, new Vector3f(0, 1, 0));
        modelMatrix.rotate(gameObject.getRotation().z, new Vector3f(0, 0, 1));
        modelMatrix.scale(gameObject.getScale());
        return modelMatrix;
    }

    public Matrix4f getModelMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
        modelMatrix.identity();
        modelMatrix.translate(position);
        modelMatrix.rotate(rotation.x, new Vector3f(1, 0, 0));
        modelMatrix.rotate(rotation.y, new Vector3f(0, 1, 0));
        modelMatrix.rotate(rotation.z, new Vector3f(0, 0, 1));
        modelMatrix.scale(scale);
        return modelMatrix;
    }

    public Matrix4f getViewMatrix(Camera camera) {
        viewMatrix.identity();
        viewMatrix.translate(camera.getPosition());
        viewMatrix.rotate(camera.getRotation().x, new Vector3f(1, 0, 0));
        viewMatrix.rotate(camera.getRotation().y, new Vector3f(0, 1, 0));
        viewMatrix.rotate(camera.getRotation().z, new Vector3f(0, 0, 1));
        return viewMatrix;
    }
}
