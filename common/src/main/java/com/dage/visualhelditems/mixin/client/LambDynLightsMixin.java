package com.dage.visualhelditems.mixin.client;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.dage.visualhelditems.CobblemonHeldItems;
import dev.lambdaurora.lambdynlights.LambDynLights;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Josh
 */


@Mixin(LambDynLights.class)
public class LambDynLightsMixin {

    @Inject(method = "getLivingEntityLuminanceFromItems", at = @At(value = "RETURN"), cancellable = true)
    private static void getLuminance(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof PokemonEntity){
            int luminance = 0;
            boolean submergedInFluid = false;
            ItemStack item = CobblemonHeldItems.INSTANCE.getHeldItem((PokemonEntity)entity);

            if(!item.isEmpty()) {
                luminance = LambDynLights.getLuminanceFromItemStack(item, submergedInFluid);
                cir.setReturnValue(luminance);
            }
        }
    }

}
