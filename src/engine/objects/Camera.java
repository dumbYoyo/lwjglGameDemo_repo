package engine.objects;

import engine.input.MouseListener;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0f;
    private float sensitivity = 8;

    public Camera() {
        position = new Vector3f(0, 0, 0);
    }

    public void thirdPersonMovement(Entity player, float dt) {
        float vd = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
        float hd = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
        float theta = player.getRotation().y + angleAroundPlayer;
        float x = (float) (hd * Math.sin(Math.toRadians(theta)));
        float z = (float) (hd * Math.cos(Math.toRadians(theta)));
        distanceFromPlayer -= MouseListener.getScrollY() * 6;
        if (MouseListener.mouseButtonDown(0)) {
            pitch -= MouseListener.getDy() * sensitivity * dt;
        }
        if (MouseListener.mouseButtonDown(0))
            angleAroundPlayer += MouseListener.getDx() * sensitivity * dt;
        yaw = 180 - theta;
        position = new Vector3f(player.getPosition().x - x, player.getPosition().y + vd, player.getPosition().z - z);

        if (distanceFromPlayer <= 5)
            distanceFromPlayer = 5;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
