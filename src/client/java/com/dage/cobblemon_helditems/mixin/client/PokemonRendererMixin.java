
package com.dage.cobblemon_helditems.mixin.client;


import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.*;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.dage.cobblemon_helditems.CobblemonHeldItemsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Map;


@Mixin(value = PokemonRenderer.class)
abstract class PokemonRendererMixin {

    @Unique
    private final HeldItemRenderer heldItemRenderer = new HeldItemRenderer(MinecraftClient.getInstance(), MinecraftClient.getInstance().getEntityRenderDispatcher(), MinecraftClient.getInstance().getItemRenderer());

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, MatrixStack poseMatrix, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {

        ItemStack heldItem = CobblemonHeldItemsClient.getCachedHeldItem(entity.getUuid());

        if (!heldItem.isEmpty()) {

            PokemonPoseableModel model = PokemonModelRepository.INSTANCE.getPoser(entity.getPokemon().getSpecies().resourceIdentifier, entity.getAspects());

            Map<String, MatrixWrapper> locators = ((PoseableEntityModel<PokemonEntity>) model).getState(entity).getLocatorStates();
            poseMatrix.push();
            if (locators.containsKey("held_item")) {
                poseMatrix.multiplyPositionMatrix( locators.get("held_item").getMatrix() );

                poseMatrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                poseMatrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, poseMatrix, buffer, packedLight);
            }
            else if (locators.containsKey("held_item_fixed")) {
                poseMatrix.multiplyPositionMatrix( locators.get("held_item_fixed").getMatrix() );

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.GROUND, false, poseMatrix, buffer, packedLight);
            }
            poseMatrix.pop();
        }
    }
}

