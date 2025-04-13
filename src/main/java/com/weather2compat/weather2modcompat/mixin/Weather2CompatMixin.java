package com.weather2compat.weather2modcompat.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import weather2.ServerTickHandler;
import weather2.weathersystem.storm.StormObject;
import net.minecraft.core.BlockPos;


@Mixin(Level.class)
public abstract class Weather2CompatMixin{

    @Inject(method = "Lnet/minecraft/world/level/Level;isRainingAt(Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void injectIsInWaterOrRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        Level level = (Level) (Object) this;

        // Custom logic here
        double maxStormDist = ((double) (512)) / 4 * 3;

        Vec3 plPos = new Vec3(pos.getX(), StormObject.static_YPos_layer0, pos.getZ());
        StormObject storm = ServerTickHandler.getWeatherManagerFor(level).getClosestStorm(plPos, maxStormDist,
                StormObject.STATE_FORMING, -1, true);

        if (storm != null) {

            float sizeToUse = storm.size;

            double stormDist = storm.pos.distanceTo(plPos);

            if (!(sizeToUse > stormDist)) {
                cir.setReturnValue(false);
            } else if (!level.canSeeSky(pos)) { // Use shadowed canSeeSky method
                cir.setReturnValue(false);
            } else if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) { // Use shadowed getHeightmapPos method
                cir.setReturnValue(false);
            } else {
                Biome biome = level.getBiome(pos).value(); // Use shadowed getBiome method
                cir.setReturnValue(biome.getPrecipitationAt(pos) == Biome.Precipitation.RAIN);
            }
        } else {
            cir.setReturnValue(false);
        }
        cir.cancel();
    }
}