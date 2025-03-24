package com.petrolpark.destroy.mixin;

<<<<<<< HEAD
=======
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
>>>>>>> parent of f19b87f6 (🛢️ Temporary fix for TFMG compatibility)
import com.petrolpark.destroy.DestroyBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidPropagator.class)
public class FluidPropagatorMixin {

    @Redirect(
        method = "Lcom/simibubi/create/content/fluids/FluidPropagator;propagateChangedPipe(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            remap = false
        ),
        remap=false
    )
<<<<<<< HEAD
    @SuppressWarnings("rawtypes")
    private static boolean matchOtherPumps(BlockEntry instance, BlockState state) {
        return instance.has(state) || DestroyBlocks.CREATIVE_PUMP.has(state);
    };
=======
    private static boolean matchOtherPumps(BlockEntry<?> instance, BlockState state, Operation<Boolean> original) {
        return DestroyBlocks.CREATIVE_PUMP.has(state) || original.call(instance, state);
    }
>>>>>>> parent of f19b87f6 (🛢️ Temporary fix for TFMG compatibility)
}
