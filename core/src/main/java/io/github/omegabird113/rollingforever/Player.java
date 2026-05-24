package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private static final float SPEED = 12f;
    private static final float FRICTION = 10f;

    private Texture texturePlayer;
    private Model modelPlayer;
    private ModelInstance instance;

    public ModelInstance create(ModelBuilder modelBuilder) {
        texturePlayer = new Texture(Gdx.files.internal("player.png"), true);
        texturePlayer.setFilter(Texture.TextureFilter.MipMapNearestNearest, Texture.TextureFilter.Nearest);
        texturePlayer.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texturePlayer);
        textureRegion.setRegion(0,0,texturePlayer.getWidth(), texturePlayer.getHeight());

        modelPlayer = modelBuilder.createSphere(3f, 3f, 3f, 24, 4,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal
                | VertexAttributes.Usage.TextureCoordinates);

        instance = new ModelInstance(modelPlayer, position);
        return instance;
    }

    public void update(float delta) {
        input.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) input.z += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) input.z -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) input.x += 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) input.x -= 1f;

        if (!input.isZero()) {
            input.nor();
            velocity.x = input.x * SPEED;
            velocity.z = input.z * SPEED;
        } else {
            velocity.x = approachZero(velocity.x, FRICTION * delta);
            velocity.z = approachZero(velocity.z, FRICTION * delta);
        }

        position.mulAdd(velocity, delta);
        instance.transform.setTranslation(position);
    }

    private float approachZero(float value, float amount) {
        if (value > 0f) return Math.max(0f, value - amount);
        if (value < 0f) return Math.min(0f, value + amount);
        return 0f;
    }

    public void dispose() {
        modelPlayer.dispose();
        texturePlayer.dispose();
    }
}
