package io.github.patbattb.yougile.plugins.deadlines;

import io.github.patbattb.plugins.core.Plugin;
import io.github.patbattb.plugins.core.expection.PluginCriticalException;
import io.github.patbattb.plugins.core.expection.PluginInterruptedException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


public class DeadlinesPlugin extends Plugin {

    private final String configFileName = "deadlines.config";

    @Override
    public String getTitle() {
        return "Deadlines";
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public int timeout() {
        return 60;
    }

    @Override
    public void run() throws PluginInterruptedException, PluginCriticalException {

        Path configFile = getConfigFilePath();
        Parameters parameters = new Parameters(configFile);

        try (TaskHandler taskHandler = new TaskHandler(parameters)) {
            taskHandler.processDailyColumn();
            taskHandler.processWeeklyColumn();
            taskHandler.shutdown();
        }
    }

    private Path getConfigFilePath() {
        String path = DeadlinesPlugin.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        return Path.of(decodedPath).getParent().resolve(configFileName);
    }
}
