package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Player {
    public final Vector3 position = new Vector3(0f, 1.5f, 0f);
    private final Vector3 velocity = new Vector3();
    private final Vector3 input = new Vector3();
    private final Vector3 movementForce = new Vector3();

    private static final float FORCE = 29f;
    private static final float ACTIVE_DRAG = 0.57f;
    private static final float COAST_DRAG = 0.15f;
    private static final float MAX_SPEED = 12f;
    private static final float WALL_BOUNCE = 0.4f;
    private static final float RADIUS = 1.5f;

    private Texture texturePlayer;
    private Model modelPlayer;
    private ModelInstance instance;

    public ModelInstance create(ModelBuilder modelBuilder) {
        texturePlayer = new Texture(Gdx.files.internal("player.png"), true);
        texturePlayer.setFilter(Texture.TextureFilter.MipMapNearestNearest, Texture.TextureFilter.Nearest);
        texturePlayer.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        modelPlayer = modelBuilder.createSphere(3f, 3f, 3f, 30, 30,
            new Material(TextureAttribute.createDiffuse(texturePlayer)),
            VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal
                | VertexAttributes.Usage.TextureCoordinates);

        instance = new ModelInstance(modelPlayer, position);
        return instance;
    }

    public void update(float delta) {
        readInput();
        applyMovementForce(delta);
        applyDrag(delta);
        limitSpeed();
        moveWithCollision(delta);
        instance.transform.setTranslation(position);
    }

    private void readInput() {
        input.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) input.z += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) input.z -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) input.x += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) input.x -= 1f;

        if (!input.isZero())
            input.nor();
    }

    private void applyMovementForce(float delta) {
        movementForce.set(input.x, 0f, input.z).scl(FORCE);
        velocity.mulAdd(movementForce, delta);
    }

    private void applyDrag(float delta) {
        float drag = input.isZero() ? COAST_DRAG : ACTIVE_DRAG;
        float dragFactor = Math.max(0f, 1f - drag * delta);
        velocity.x *= dragFactor;
        velocity.z *= dragFactor;
    }

    private void limitSpeed() {
        float speed2 = velocity.x * velocity.x + velocity.z * velocity.z;
        if (speed2 > MAX_SPEED * MAX_SPEED) {
            float speed = (float)Math.sqrt(speed2);
            velocity.x = velocity.x / speed * MAX_SPEED;
            velocity.z = velocity.z / speed * MAX_SPEED;
        }
    }

    private void moveWithCollision(float delta) {
        float nextX = position.x + velocity.x * delta;
        float nextZ = position.z + velocity.z * delta;

        position.x = nextX;
        if (CollisionManager.collidesWithWall(position, RADIUS)) {
            position.x -= velocity.x * delta;
            velocity.x = -velocity.x * WALL_BOUNCE;
        }

        position.z = nextZ;
        if (CollisionManager.collidesWithWall(position, RADIUS)) {
            position.z -= velocity.z * delta;
            velocity.z = -velocity.z * WALL_BOUNCE;
        }
    }

    public void dispose() {
        modelPlayer.dispose();
        texturePlayer.dispose();
    }
}
