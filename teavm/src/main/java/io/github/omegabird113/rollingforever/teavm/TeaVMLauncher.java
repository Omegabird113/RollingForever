package io.github.omegabird113.rollingforever.teavm;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import io.github.omegabird113.rollingforever.Main;

public class TeaVMLauncher {
    public static void main(String[] args) {
        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        //// If width and height are both 0, then the app will use all available space.
        config.width = 0;
        config.height = 0;
        new WebApplication(new Main(), config);
    }
}
