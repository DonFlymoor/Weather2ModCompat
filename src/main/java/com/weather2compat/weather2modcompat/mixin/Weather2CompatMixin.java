package com.weather2compat.weather2modcompat.mixin;

import net.minecraft.world.level.LevelAccessor;
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
public abstract class Weather2CompatMixin {

    // Shadow method for Level#getHeightmapPos(Heightmap.Types, BlockPos)
    @Shadow
    public abstract BlockPos getHeightmapPos(Heightmap.Types heightmapType, BlockPos pos);

    // Shadow method for Level#canSeeSky(BlockPos)
    @Shadow
    public abstract boolean canSeeSky(BlockPos pos);

    // Shadow method for Level#getBiome(BlockPos)
    @Shadow
    public abstract net.minecraft.core.Holder<Biome> getBiome(BlockPos pos);

    @Shadow
    public net.minecraft.world.level.Level levelData;

    @Inject(method = "isRainingAt", at = @At("HEAD"), cancellable = true)
    private void redirectIsInWaterOrRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        // Custom logic here
        double maxStormDist = ((double) (512)) / 4 * 3;

        Vec3 plPos = new Vec3(pos.getX(), StormObject.static_YPos_layer0, pos.getZ());
        StormObject storm = ServerTickHandler.getWeatherManagerFor(levelData).getClosestStorm(plPos, maxStormDist,
                StormObject.STATE_FORMING, -1, true);

        double stormDist = 9999;

        float sizeToUse = 0;

        if (storm != null) {

            sizeToUse = storm.size;

            stormDist = storm.pos.distanceTo(plPos);

            if (!(sizeToUse > stormDist)) {
                cir.setReturnValue(false);
            } else if (!this.canSeeSky(pos)) { // Use shadowed canSeeSky method
                cir.setReturnValue(false);
            } else if (this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) { // Use shadowed getHeightmapPos method
                cir.setReturnValue(false);
            } else {
                Biome biome = this.getBiome(pos).value(); // Use shadowed getBiome method
                cir.setReturnValue(biome.getPrecipitationAt(pos) == Biome.Precipitation.RAIN);
            }
        } else {
            cir.setReturnValue(false);
        }
        cir.cancel();
    }
}