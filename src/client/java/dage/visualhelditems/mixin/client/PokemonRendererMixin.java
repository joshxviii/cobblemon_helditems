
package dage.visualhelditems.mixin.client;

import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.*;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dage.visualhelditems.CobblemonHeldItems;
import dage.visualhelditems.CobblemonHeldItemsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;


@Mixin(value = PokemonRenderer.class)
abstract class PokemonRendererMixin {

    @Unique
    private final HeldItemRenderer heldItemRenderer = new HeldItemRenderer(MinecraftClient.getInstance(), MinecraftClient.getInstance().getEntityRenderDispatcher(), MinecraftClient.getInstance().getItemRenderer());

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, MatrixStack poseMatrix, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {

        ItemStack heldItem = CobblemonHeldItemsClient.getHeldItem(entity.getUuid());

        if (!heldItem.isEmpty()) {
            PokemonPoseableModel model = PokemonModelRepository.INSTANCE.getPoser(entity.getPokemon().getSpecies().resourceIdentifier, entity.getAspects());
            PoseableEntityState<PokemonEntity> state = ((PoseableEntityModel<PokemonEntity>) model).getState(entity);
            Map<String, MatrixWrapper> locators = state.getLocatorStates();

            poseMatrix.push();

          //For version 0.2.0

//          if(heldItem.isIn(CobblemonHeldItems.WEARABLE_EYE_ITEMS) && locators.containsKey("eyes"))/*render wearable glasses items*/ {
//              poseMatrix.multiplyPositionMatrix( locators.get("eyes").getMatrix() );
//              poseMatrix.translate(0f,0f,.28f);
//              poseMatrix.scale(0.7f,0.7f,0.7f);
//              this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.HEAD, false, poseMatrix, buffer, packedLight);
//          }
//          else if(heldItem.isIn(CobblemonHeldItems.WEARABLE_HAT_ITEMS) && locators.containsKey("head_top"))/*render wearable hat items*/ {
//              poseMatrix.multiplyPositionMatrix( locators.get("head_top").getMatrix() );
//              poseMatrix.translate(0f,-0.26f, 0f);
//              poseMatrix.scale(.68f,.68f,.68f);
//              this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.HEAD, false, poseMatrix, buffer, packedLight);
//          }
//          else


            if (locators.containsKey("held_item"))/*render item the same way as the player*/ {
                poseMatrix.multiplyPositionMatrix(locators.get("held_item").getMatrix());

                poseMatrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                poseMatrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, poseMatrix, buffer, packedLight);
            } else if (locators.containsKey("held_item_fixed"))/*render flat ground item model*/ {
                poseMatrix.multiplyPositionMatrix(locators.get("held_item_fixed").getMatrix());

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.GROUND, false, poseMatrix, buffer, packedLight);
            }
            poseMatrix.pop();
        }
    }
}

