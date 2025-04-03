package com.weather2compat.weather2modcompat;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Weather2Compat.MODID)
public class Weather2Compat {
    public static final String MODID = "weather2modcompat";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Weather2Compat() {
        // Initialize Mixin framework
        initMixins();

        // Get the mod event bus
        //noinspection removal
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the common setup method to the mod event bus
        modEventBus.addListener(this::commonSetup);

        // Register this class for global events
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Initialize Mixins by registering the configuration file.
     */
    private void initMixins() {
        try {
            // This initializes the Mixin bootstrap
            MixinBootstrap.init();

            // Add the Mixin configuration file
            Mixins.addConfiguration("mixins.weather2modcompat.json");

            LOGGER.info("Weather2Compat: Mixins initialized successfully.");
        } catch (Exception e) {
            LOGGER.error("Weather2Compat: Failed to initialize Mixins!", e);
        }
    }

    /**
     * Common setup method, called during the mod's initialization phase.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Weather2Compat: Common setup complete.");
    }
}