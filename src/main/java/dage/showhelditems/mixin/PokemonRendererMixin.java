
package dage.showhelditems.mixin;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dage.showhelditems.NullObjectParser;
import dage.showhelditems.ShowHeldItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;


@Mixin(value = PokemonRenderer.class)
abstract class PokemonRendererMixin {

    @Unique
    private final ItemInHandRenderer heldItemRenderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());

    @Unique
    float scale;
    @Unique
    ItemDisplayContext transformationMode;

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseMatrix, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        scale = 1.0f;
        transformationMode = ItemDisplayContext.GROUND;

        ItemStack heldItem = ShowHeldItems.INSTANCE.getHeldItem(entity);

        if (!heldItem.isEmpty()) {

            PokemonClientDelegate clientDelegate = (PokemonClientDelegate) entity.getDelegate();
            Map<String, MatrixWrapper> locators = clientDelegate.getLocatorStates();

            poseMatrix.pushPose();

            //For version 0.2.0

            if(heldItem.is(ShowHeldItems.WEARABLE_EYE_ITEMS) && locators.containsKey("held_item_eyes"))/*render wearable glasses items*/ {
                poseMatrix.mulPose( locators.get("held_item_eyes").getMatrix() );
                transformationMode = ItemDisplayContext.HEAD;
                applyModifiers("held_item_eyes", locators);
                poseMatrix.translate(0f,0f,.28f*scale);
                poseMatrix.scale(0.7f*scale,0.7f*scale,0.7f*scale);
            }
            else if(heldItem.is(ShowHeldItems.WEARABLE_HAT_ITEMS) && locators.containsKey("held_item_head"))/*render wearable hat items*/ {
                poseMatrix.mulPose( locators.get("held_item_head").getMatrix() );
                transformationMode = ItemDisplayContext.HEAD;
                applyModifiers("held_item_head", locators);
                poseMatrix.translate(0f,-0.26f*scale, 0f);
                poseMatrix.scale(.68f*scale,.68f*scale,.68f*scale);
            }
            else if (locators.containsKey("held_item"))/*render item the same way as the player*/ {
                poseMatrix.mulPose(locators.get("held_item").getMatrix());
                transformationMode = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                applyModifiers("held_item", locators);
                poseMatrix.scale(scale, scale, scale);
                poseMatrix.mulPose((Axis.XP.rotationDegrees(-90)));
                poseMatrix.mulPose(Axis.YP.rotationDegrees(-90));
            }
            else if (locators.containsKey("held_item_fixed"))/*render flat ground item model*/ {
                poseMatrix.mulPose(locators.get("held_item_fixed").getMatrix());
                applyModifiers("held_item_fixed", locators);
                poseMatrix.scale(scale, scale, scale);
            }
            else {poseMatrix.popPose();return;}

            this.heldItemRenderer.renderItem(entity, heldItem, transformationMode, false, poseMatrix, buffer, packedLight);
            poseMatrix.popPose();
        }
    }


    @Unique
    private void applyModifiers(String name, Map<String, MatrixWrapper> locators) {
        locators.forEach((locator,m)->{
            Map<String, Float> modifiers;
            if (locator.startsWith("_null_"+name+"[")) {
                modifiers = NullObjectParser.parseNullObject(locator).getModifiers();
                ItemDisplayContext[] modes = ItemDisplayContext.values();
                if ( modifiers.containsKey("scale") ) scale = modifiers.get("scale");
                if ( modifiers.containsKey("mode") ) transformationMode = modes[( (int)(float)modifiers.get("mode") ) % modes.length];
                return;
            }
        });
    }
}

