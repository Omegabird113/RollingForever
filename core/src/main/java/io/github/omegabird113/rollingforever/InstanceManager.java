package io.github.omegabird113.rollingforever;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.omegabird113.rollingforever.objects.Player;
import io.github.omegabird113.rollingforever.objects.Room;
import io.github.omegabird113.rollingforever.objects.Sky;
import io.github.omegabird113.rollingforever.utils.CameraFollowUtils;
import io.github.omegabird113.rollingforever.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InstanceManager {
    public final ArrayList<ModelInstance> instances = new ArrayList<>();
    private final Room room = new Room();
    private final Player player = new Player();
    private final Sky sky = new Sky();

    public ArrayList<ModelInstance> getInstances() {
        return instances;
    }

    public void add(ModelInstance instance) {
        instances.add(instance);
    }

    public void add(Collection<ModelInstance> instances) {
        this.instances.addAll(instances);
    }

    public void add(ModelInstance[] instances) {
        this.instances.addAll(List.of(instances));
    }

    void render(float delta, ModelBatch modelBatch, PerspectiveCamera camera, Environment environment) {
        player.update(delta);
        CameraFollowUtils.updateCameraTo(camera, player.getPosition());
        camera.update();
        sky.update(camera.position);

        ScreenUtils.clear(ColorUtils.BLACK, true);
        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    void createInitial(ModelBuilder modelBuilder) {
        this.add(sky.create(modelBuilder));
        this.add(room.create(modelBuilder));
        this.add(player.create(modelBuilder));
    }

    void disposeAll() {
        room.dispose();
        player.dispose();
        sky.dispose();
    }
}
