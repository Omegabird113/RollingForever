package io.github.omegabird113.rollingforever.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import io.github.omegabird113.rollingforever.physics.RollingMovement;

public class Player {
    private final RollingMovement rollingMovement = new RollingMovement(29f, 0.57f, 0.24f, 12.35f, 0.47f, 1.5f);
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

        instance = new ModelInstance(modelPlayer, getPosition());
        return instance;
    }

    public void update(float delta) {
        Vector3 newPosition = rollingMovement.move(delta);
        instance.transform.setTranslation(newPosition);
    }

    public Vector3 getPosition() {
        return rollingMovement.getPosition();
    }

    public void dispose() {
        modelPlayer.dispose();
        texturePlayer.dispose();
    }
}
