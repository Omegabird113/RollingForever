package io.github.omegabird113.rollingforever.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import io.github.omegabird113.rollingforever.physics.CollisionManager;
import io.github.omegabird113.rollingforever.physics.WallBounds;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Model modelGround;
    private Model modelWallEast;
    private Model modelWallWest;
    private Model modelWallNorth;
    private Model modelWallSouth;
    private Texture textureGround;
    private Texture textureWalls;

    private ModelInstance createGround(ModelBuilder modelBuilder) {
        textureGround = new Texture(Gdx.files.internal("ground.png"), true);
        textureGround.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Nearest);
        textureGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(textureGround);
        int repeats = 8;
        textureRegion.setRegion(0, 0, textureGround.getWidth() * repeats, textureGround.getHeight() * repeats);

        // create model
        modelGround = modelBuilder.createBox(25f, 1f, 25f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        return new ModelInstance(modelGround, 0, -1, 0);
    }

    private ModelInstance[] createWalls(ModelBuilder modelBuilder) {
        textureWalls = new Texture(Gdx.files.internal("walls.png"), true);
        textureWalls.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Nearest);
        textureWalls.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(textureWalls);
        int repeats = 6;
        textureRegion.setRegion(0, 0, textureGround.getWidth(), textureGround.getHeight() * repeats);

        modelWallEast = modelBuilder.createBox(0.5f, 6f, 24.5f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        modelWallWest = modelBuilder.createBox(0.5f, 6f, 24.5f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        modelWallNorth = modelBuilder.createBox(25.5f, 6f, 0.5f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        modelWallSouth = modelBuilder.createBox(25.5f, 6f, 0.5f,
            new Material(TextureAttribute.createDiffuse(textureRegion)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        ModelInstance[] models = new ModelInstance[]{
            new ModelInstance(modelWallEast, 12.5f, 2f, 0f),
            new ModelInstance(modelWallWest, -12.5f, 2f, 0f),
            new ModelInstance(modelWallNorth, 0f, 2f, 12.5f),
            new ModelInstance(modelWallSouth, 0f, 2f, -12.5f)
        };

        for (ModelInstance modelInstance : models) {
            CollisionManager.addWallBounds(new WallBounds(modelInstance));
        }

        return models;
    }

    public ModelInstance[] create(ModelBuilder modelBuilder) {
        ArrayList<ModelInstance> modelInstances = new ArrayList<>();
        ModelInstance ground = createGround(modelBuilder);
        modelInstances.add(ground);
        ModelInstance[] walls = createWalls(modelBuilder);
        modelInstances.addAll(List.of(walls));
        return modelInstances.toArray(new ModelInstance[0]);
    }

    private void disposeGround() {
        modelGround.dispose();
        textureGround.dispose();
    }

    private void disposeWalls() {
        textureWalls.dispose();
        modelWallEast.dispose();
        modelWallWest.dispose();
        modelWallNorth.dispose();
        modelWallSouth.dispose();
    }

    public void dispose() {
        disposeGround();
        disposeWalls();
    }
}
