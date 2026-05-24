package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Player {
    private Texture texturePlayer;
    private Model modelPlayer;

    public ModelInstance create(ModelBuilder modelBuilder) {
        texturePlayer = new Texture(Gdx.files.internal("player.png"), true);
        texturePlayer.setFilter(Texture.TextureFilter.MipMapNearestNearest, Texture.TextureFilter.Nearest);
        texturePlayer.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texturePlayer);
        textureRegion.setRegion(0,0,texturePlayer.getWidth(), texturePlayer.getHeight());

        // create model
        modelPlayer = modelBuilder.createSphere(3f, 3f, 3f, 24, 4,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal
                | VertexAttributes.Usage.TextureCoordinates);

        return new ModelInstance(modelPlayer, 0, 3, 0);
    }

    public void dispose() {
        modelPlayer.dispose();
        texturePlayer.dispose();
    }
}
