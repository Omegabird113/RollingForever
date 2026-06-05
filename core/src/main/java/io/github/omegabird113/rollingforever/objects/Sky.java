/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.omegabird113.rollingforever.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Sky {
    private static final float SPHERE_RADIUS = 250f;
    private static final int HORIZONTAL_SEGMENTS = 96;
    private static final int VERTICAL_SEGMENTS = 48;

    private Texture textureSky;
    private Model modelSky;
    private ModelInstance instance;

    public ModelInstance create(ModelBuilder modelBuilder) {
        textureSky = loadSkyTexture();

        modelBuilder.begin();
        MeshPartBuilder meshBuilder = createSkySpherePart(modelBuilder);
        buildSphereGridMesh(meshBuilder);
        modelSky = modelBuilder.end();

        instance = new ModelInstance(modelSky);
        return instance;
    }

    private Texture loadSkyTexture() {
        Texture texture = new Texture(Gdx.files.internal("skybox.png"), false);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        return texture;
    }

    private MeshPartBuilder createSkySpherePart(ModelBuilder modelBuilder) {
        Material material = new Material(
            new TextureAttribute(TextureAttribute.Diffuse, textureSky),
            IntAttribute.createCullFace(GL20.GL_NONE) // render the inside surface
        );

        int vertexAttributes = VertexAttributes.Usage.Position
            | VertexAttributes.Usage.Normal
            | VertexAttributes.Usage.TextureCoordinates;

        return modelBuilder.part("skyBox", GL20.GL_TRIANGLES, vertexAttributes, material);
    }

    private void buildSphereGridMesh(MeshPartBuilder meshBuilder) {
        for (int row = 0; row < VERTICAL_SEGMENTS; row++) {
            for (int col = 0; col < HORIZONTAL_SEGMENTS; col++) {
                MeshPartBuilder.VertexInfo bottomLeft = createVertexInfo(row, col);
                MeshPartBuilder.VertexInfo bottomRight = createVertexInfo(row, col + 1);
                MeshPartBuilder.VertexInfo topRight = createVertexInfo(row + 1, col + 1);
                MeshPartBuilder.VertexInfo topLeft = createVertexInfo(row + 1, col);

                meshBuilder.triangle(bottomLeft, topRight, topLeft);
                meshBuilder.triangle(bottomLeft, bottomRight, topRight);
            }
        }
    }

    private MeshPartBuilder.VertexInfo createVertexInfo(int row, int col) {
        float verticalProgress = row / (float) VERTICAL_SEGMENTS;
        float horizontalProgress = col / (float) HORIZONTAL_SEGMENTS;

        // -90 degrees at bottom & +90 degrees at top
        float pitch = MathUtils.lerp(-MathUtils.PI / 2f, MathUtils.PI / 2f, verticalProgress);
        float yaw = horizontalProgress * MathUtils.PI2;

        float radiusAtThisHeight = MathUtils.cos(pitch) * SPHERE_RADIUS;
        float x = MathUtils.cos(yaw) * radiusAtThisHeight;
        float y = MathUtils.sin(pitch) * SPHERE_RADIUS;
        float z = MathUtils.sin(yaw) * radiusAtThisHeight;

        Vector3 position = new Vector3(x, y, z);
        Vector3 inwardNormal = new Vector3(position).scl(-1f).nor();

        return new MeshPartBuilder.VertexInfo()
            .setPos(position)
            .setNor(inwardNormal)
            .setUV(horizontalProgress, verticalProgress);
    }

    public void update(Vector3 position) {
        instance.transform.setTranslation(position);
    }

    public void dispose() {
        modelSky.dispose();
        textureSky.dispose();
    }
}
