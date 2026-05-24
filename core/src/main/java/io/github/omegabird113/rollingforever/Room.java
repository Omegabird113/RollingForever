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

public class Room {
    private Model modelGround;
    private Texture textureGround;

    public ModelInstance createGround(ModelBuilder modelBuilder) {
        textureGround = new Texture(Gdx.files.internal("ground.png"), true);
        textureGround.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Nearest);
        textureGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(textureGround);
        int repeats = 4;
        textureRegion.setRegion(0,0,textureGround.getWidth()*repeats, textureGround.getHeight()*repeats );

        // create model
        modelGround = modelBuilder.createBox(25f, 1f, 25f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        return new ModelInstance(modelGround, 0, -1, 0);
    }

    public void disposeGround() {
        modelGround.dispose();
        textureGround.dispose();
    }
}
