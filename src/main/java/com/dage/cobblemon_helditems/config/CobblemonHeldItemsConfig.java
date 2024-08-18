package com.dage.cobblemon_helditems.config;

import com.dage.cobblemon_helditems.CobblemonHeldItems;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Scanner;

/**
 * @author Josh
 */
public class CobblemonHeldItemsConfig {

    //TODO setup server config file
    public static final Logger LOGGER = LoggerFactory.getLogger("cobblemon_helditems");
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final File file;

    public CobblemonHeldItemsConfig(File file) {
        this.file = file;
    }

    public static final CobblemonHeldItemsConfig INSTANCE = new CobblemonHeldItemsConfig(FabricLoader.getInstance().getConfigDir().resolve(CobblemonHeldItems.MOD_ID + ".properties").toFile());
    static {
        try {
            INSTANCE.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//
    public void load() throws IOException {
        if (file.exists()) {
            try (Scanner reader = new Scanner( file )) {
                reader.hasNextLine();
            } catch (Exception e) {
                LOGGER.error("Could not load config from file '" + file.getAbsolutePath() + "'", e);
            }
        }
        else createConfig();
    }

    public void createConfig() throws IOException {
        FileAttribute<?> fileAttribute = new FileAttribute() {
            @Override
            public String name() {
                return null;
            }

            @Override
            public Object value() {
                return null;
            }
        };
        Files.createFile(file.toPath(), fileAttribute);
    }
}
