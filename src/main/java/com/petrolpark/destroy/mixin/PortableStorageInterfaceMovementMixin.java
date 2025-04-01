package com.petrolpark.destroy.mixin;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PortableStorageInterfaceMovement.class)
public class PortableStorageInterfaceMovementMixin {
    /**
     * This isn't related to Destroy whatsoever but my farms keep getting stuck and
     * it's pissing me off so fuck it, I'm doing it here.
     */
    @Inject(
        method = "Lcom/simibubi/create/content/contraptions/actors/psi/PortableStorageInterfaceMovement;tick(Lcom/simibubi/create/content/contraptions/behaviour/MovementContext;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;)Z",
            remap = true
        ),
        remap = false,
        cancellable = true
    )
    private void fixStalledInterface(MovementContext context, CallbackInfo ci) {
        // If the PSI has no working position set, unstall it.
        // I haven't found the actual root cause for this but I suspect it happens when the chunk gets unloaded right before a PSI disengages.
        if(!context.data.contains("WorkingPos")) {
            ((PortableStorageInterfaceMovement)(Object)this).cancelStall(context);
            ci.cancel();
        }
    }
}
