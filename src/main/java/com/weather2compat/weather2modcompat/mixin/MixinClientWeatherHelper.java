package com.weather2compat.weather2modcompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import weather2.ClientTickHandler;
import weather2.ClientWeatherHelper;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(ClientWeatherHelper.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinClientWeatherHelper {

    @Inject(method = "ClientWeatherHelper/getPrecipitationStrength", at = @At("RETURN"))
    public void forceClientRain(Player entP, boolean forOvercast, CallbackInfoReturnable<Float> cir,@Local(ordinal = 0) boolean closeEnough) {
        if (closeEnough) { // Directly use closeEnough as it is defined in the target method
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().level.getLevelData().setRaining(true);
            }
        } else {
            if (Minecraft.getInstance().level != null
                    && !ClientTickHandler.weatherManager.isVanillaRainActiveOnServer) {
                Minecraft.getInstance().level.getLevelData().setRaining(false);
            }
        }
    }
}
