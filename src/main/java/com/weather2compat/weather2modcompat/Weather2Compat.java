package com.weather2compat.weather2modcompat;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Weather2Compat.MODID)
public class Weather2Compat {
    public static final String MODID = "weather2modcompat";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public Weather2Compat() {
        // Get the mod event bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the common setup method to the mod event bus
        modEventBus.addListener(this::commonSetup);

        // Register this class for server and other game events on the global event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Common setup method, called during the mod's initialization phase.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Weather2Compat: Common setup complete.");
    }

    /**
     * Event handler for when the server is starting.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Weather2Compat: Server starting.");
    }

    /**
     * Client-specific event handlers.
     */
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        /**
         * Event handler for client setup.
         */
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LogUtils.getLogger().info("Weather2Compat: Client setup complete.");
        }
    }
}