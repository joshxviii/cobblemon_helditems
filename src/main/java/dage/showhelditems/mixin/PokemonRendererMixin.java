
package dage.showhelditems.mixin;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dage.showhelditems.NullObjectParser;
import dage.showhelditems.ShowHeldItems;
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

    @Unique
    float scale;
    @Unique
    ModelTransformationMode transformationMode;

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, MatrixStack poseMatrix, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {
        scale = 1.0f;
        transformationMode = ModelTransformationMode.GROUND;

        ItemStack heldItem = ShowHeldItems.INSTANCE.getHeldItem(entity);

        if (!heldItem.isEmpty()) {

            PokemonClientDelegate clientDelegate = (PokemonClientDelegate) entity.getDelegate();
            Map<String, MatrixWrapper> locators = clientDelegate.getLocatorStates();

            poseMatrix.push();

            //For version 0.2.0

            if(heldItem.isIn(ShowHeldItems.INSTANCE.getWEARABLE_EYE_ITEMS()) && locators.containsKey("held_item_eyes"))/*render wearable glasses items*/ {
                poseMatrix.multiplyPositionMatrix( locators.get("held_item_eyes").getMatrix() );
                transformationMode = ModelTransformationMode.HEAD;
                applyModifiers("held_item_eyes", locators);
                poseMatrix.translate(0f,0f,.28f*scale);
                poseMatrix.scale(0.7f*scale,0.7f*scale,0.7f*scale);
            }
            else if(heldItem.isIn(ShowHeldItems.INSTANCE.getWEARABLE_HAT_ITEMS()) && locators.containsKey("held_item_head"))/*render wearable hat items*/ {
                poseMatrix.multiplyPositionMatrix( locators.get("held_item_head").getMatrix() );
                transformationMode = ModelTransformationMode.HEAD;
                applyModifiers("held_item_head", locators);
                poseMatrix.translate(0f,-0.26f*scale, 0f);
                poseMatrix.scale(.68f*scale,.68f*scale,.68f*scale);
            }
            else if (locators.containsKey("held_item"))/*render item the same way as the player*/ {
                poseMatrix.multiplyPositionMatrix(locators.get("held_item").getMatrix());
                transformationMode = ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                applyModifiers("held_item", locators);
                poseMatrix.scale(scale, scale, scale);
                poseMatrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                poseMatrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
            }
            else if (locators.containsKey("held_item_fixed"))/*render flat ground item model*/ {
                poseMatrix.multiplyPositionMatrix(locators.get("held_item_fixed").getMatrix());
                applyModifiers("held_item_fixed", locators);
                poseMatrix.scale(scale, scale, scale);
            }
            else {poseMatrix.pop();return;}

            this.heldItemRenderer.renderItem(entity, heldItem, transformationMode, false, poseMatrix, buffer, packedLight);
            poseMatrix.pop();
        }
    }


    @Unique
    private void applyModifiers(String name, Map<String, MatrixWrapper> locators) {
        locators.forEach((locator,m)->{
            Map<String, Float> modifiers;
            if (locator.startsWith("_null_"+name+"[")) {
                modifiers = NullObjectParser.parseNullObject(locator).getModifiers();
                ModelTransformationMode[] modes = ModelTransformationMode.values();
                if ( modifiers.containsKey("scale") ) scale = modifiers.get("scale");
                if ( modifiers.containsKey("mode") ) transformationMode = modes[( (int)(float)modifiers.get("mode") ) % modes.length];
                return;
            }
        });
    }
}

