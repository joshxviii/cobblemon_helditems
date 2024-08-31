package dage.visualhelditems.mixin.client;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dage.visualhelditems.CobblemonHeldItemsClient;
import dev.lambdaurora.lambdynlights.LambDynLights;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.logging.Level;

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
            ItemStack item = CobblemonHeldItemsClient.getHeldItem(entity.getUuid());

            if(!item.isEmpty()) {
                luminance = LambDynLights.getLuminanceFromItemStack(item, submergedInFluid);
                cir.setReturnValue(luminance);
            }
        }
    }

}
