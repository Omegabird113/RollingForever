/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
