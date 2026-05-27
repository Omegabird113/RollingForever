package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.omegabird113.rollingforever.objects.Player;
import io.github.omegabird113.rollingforever.objects.Room;
import io.github.omegabird113.rollingforever.objects.SkyBox;
import io.github.omegabird113.rollingforever.utils.CameraFollowUtils;
import io.github.omegabird113.rollingforever.utils.ColorUtils;

public class Main extends ApplicationAdapter {
    private final Room room = new Room();
    private final Player player = new Player();
    private final SkyBox skyBox = new SkyBox();
    private PerspectiveCamera camera;
    private Environment environment;
    private Array<ModelInstance> instances;
    private ModelBatch modelBatch;

    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 25f, -2f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();

        instances = new Array<>();
        instances.add(skyBox.create(modelBuilder));
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

        player.update(delta);
        CameraFollowUtils.updateCameraTo(camera, player.getPosition());
        camera.update();
        skyBox.update(camera.position);

        ScreenUtils.clear(ColorUtils.BLACK, true);
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
        room.disposeWalls();
        player.dispose();
        skyBox.dispose();
    }
}
