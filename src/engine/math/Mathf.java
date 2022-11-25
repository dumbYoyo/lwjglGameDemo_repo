package engine.math;

import engine.objects.Camera;
import engine.objects.Entity;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Mathf {
    private Matrix4f modelViewMatrix, modelMatrix, modelOrthoMatrix;
    private Matrix4f projMatrix;
    private Matrix4f viewMatrix;

    private Matrix4f ortho;

    public Mathf() {
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

    public Matrix4f getOrthoModelMatrix(Entity entity, Matrix4f ortho) {
        modelMatrix.identity();
        modelMatrix.translate(entity.getPosition());
        modelMatrix.rotate(Math.toRadians(entity.getRotation().x), new Vector3f(1, 0, 0));
        modelMatrix.rotate(Math.toRadians(entity.getRotation().y), new Vector3f(0, 1, 0));
        modelMatrix.rotate(Math.toRadians(entity.getRotation().z), new Vector3f(0, 0, 1));
        modelMatrix.scale(entity.getScale());
        return ortho.mul(modelMatrix);
    }

    public Matrix4f getOrthoModelMatrix(float posX, float posY, Matrix4f ortho) {
        modelOrthoMatrix.identity();
        modelOrthoMatrix.translate(posX, posY, 0f);

        modelOrthoMatrix.scale(1);
        return ortho.mul(modelOrthoMatrix);
    }

    public Matrix4f getModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
        modelViewMatrix.identity();
        modelViewMatrix.translate(entity.getPosition());
        modelViewMatrix.rotate(entity.getRotation().x, new Vector3f(1, 0, 0));
        modelViewMatrix.rotate(entity.getRotation().y, new Vector3f(0, 1, 0));
        modelViewMatrix.rotate(entity.getRotation().z, new Vector3f(0, 0, 1));
        modelViewMatrix.scale(entity.getScale());
        return viewMatrix.mul(modelViewMatrix);
    }

    public Matrix4f getModelMatrix(Entity entity) {
        modelMatrix.identity();
        modelMatrix.translate(entity.getPosition());
        modelMatrix.rotate(entity.getRotation().x, new Vector3f(1, 0, 0));
        modelMatrix.rotate(entity.getRotation().y, new Vector3f(0, 1, 0));
        modelMatrix.rotate(entity.getRotation().z, new Vector3f(0, 0, 1));
        modelMatrix.scale(entity.getScale());
        return modelMatrix;
    }

    public Matrix4f getModelMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
        modelMatrix.identity();
        modelMatrix.translate(position);
        modelMatrix.rotate(Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        modelMatrix.rotate(Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        modelMatrix.rotate(Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        modelMatrix.scale(scale);
        return modelMatrix;
    }

    public Matrix4f getViewMatrix(Camera camera) {
        // eq 4, then 3, then 2, then 1 - this order is read as translate * rotate, which makes the object rotate around ITS local origin -> first option (a)
        // eq 1, then 2, then 3, then 4 - this is order is read as rotate * translate, which makes the object rotate around the world origin -> second option (b)
        // we would like the camera to rotate around ITS local origin, so we do - first option(a).

        viewMatrix.identity();
        viewMatrix.rotate(Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0)); // eq - 4
        viewMatrix.rotate(Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0)); // eq - 3
        viewMatrix.rotate(Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1)); // eq - 2
        viewMatrix.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z)); // eq - 1
        return viewMatrix;
    }
}
