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

public final class InstanceManager {
    private InstanceManager() {}

    private static final Room room = new Room();
    private static final Player player = new Player();
    private static final Sky sky = new Sky();

    public static final ArrayList<ModelInstance> instances = new ArrayList<>();

    public static ArrayList<ModelInstance> getInstances() {
        return instances;
    }

    public static void add(ModelInstance instance) {
        instances.add(instance);
    }

    public static void add(Collection<ModelInstance> instances) {
        InstanceManager.instances.addAll(instances);
    }

    public static void add(ModelInstance[] instances) {
        InstanceManager.instances.addAll(List.of(instances));
    }

    static void render(float delta, ModelBatch modelBatch, PerspectiveCamera camera, Environment environment) {
        player.update(delta);
        CameraFollowUtils.updateCameraTo(camera, player.getPosition());
        camera.update();
        sky.update(camera.position);

        ScreenUtils.clear(ColorUtils.BLACK, true);
        modelBatch.begin(camera);
        modelBatch.render(InstanceManager.getInstances(), environment);
        modelBatch.end();
    }

    static void createInitial(ModelBuilder modelBuilder) {
        InstanceManager.add(sky.create(modelBuilder));
        InstanceManager.add(room.create(modelBuilder));
        InstanceManager.add(player.create(modelBuilder));
    }

    static void disposeAll() {
        room.dispose();
        player.dispose();
        sky.dispose();
    }
}
