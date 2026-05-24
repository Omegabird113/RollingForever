package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.omegabird113.rollingforever.utils.ColorUtils;

public class Main extends ApplicationAdapter {
    private final Color BACKGROUND_COLOUR = ColorUtils.get8BitColor(153, 255, 236);

    private PerspectiveCamera camera;
    private Environment environment;
    private Array<ModelInstance> instances;
    private ModelBatch modelBatch;
    private final Room room = new Room();
    private final Player player = new Player();

    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        camera.position.set(0f, 25f, -2f);
        camera.lookAt(0,0,0);
        camera.near = 0.1f;
        camera.far = 30000f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();

        // create and position model instances
        instances = new Array<>();
        instances.add(room.createGround(modelBuilder));
        for (ModelInstance wall : room.createWalls(modelBuilder)) {
            instances.add(wall);
        }
        instances.add(player.create(modelBuilder));

        modelBatch = new ModelBatch();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(BACKGROUND_COLOUR);
        modelBatch.begin(camera);

        player.update(delta);
        followPlayerWithCamera();

        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    private void followPlayerWithCamera() {
        camera.position.set(player.position.x, player.position.y + 22f, player.position.z - 18f);
        camera.lookAt(player.position.x, player.position.y, player.position.z);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        room.disposeGround();
        room.disposeWalls();
        player.dispose();
    }
}
