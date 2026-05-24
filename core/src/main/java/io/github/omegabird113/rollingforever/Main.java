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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private PerspectiveCamera camera;
    private Environment environment;
    private Array<ModelInstance> instances;
    private ModelBatch modelBatch;
    private final Color BACKGROUND_COLOUR = new Color(153f/255f, 255f/255f, 236f/255f, 1.0f);
    private final Room room = new Room();

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

        modelBatch = new ModelBatch();
    }

    @Override
    public void render() {
        // render
        ScreenUtils.clear(BACKGROUND_COLOUR);
        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
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
    }
}
