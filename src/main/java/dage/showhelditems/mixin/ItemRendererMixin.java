package dage.showhelditems.mixin;

import com.cobblemon.mod.common.CobblemonItems;
import com.mojang.blaze3d.vertex.PoseStack;
import dage.showhelditems.ShowHeldItems;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author Josh
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useItem(BakedModel value, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (renderMode == ItemDisplayContext.HEAD) {
            ModelManager modelManager = ((ItemRenderer) (Object) this).getItemModelShaper().getModelManager();

            if (stack.is(CobblemonItems.BLACK_GLASSES)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "blackglasses"), "inventory"));
            if (stack.is(CobblemonItems.CHOICE_SPECS)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "choicespecs"), "inventory"));
            if (stack.is(CobblemonItems.SAFETY_GOGGLES)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "safetygoggles"), "inventory"));
            if (stack.is(CobblemonItems.WISE_GLASSES)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "wiseglasses"), "inventory"));
            if (stack.is(CobblemonItems.ROCKY_HELMET)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "rockyhelmet"), "inventory"));
            if (stack.is(CobblemonItems.KINGS_ROCK)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "kingsrock"), "inventory"));
            if (stack.is(CobblemonItems.EXP_SHARE)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "expshare"), "inventory"));
            if (stack.is(CobblemonItems.CHOICE_BAND)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "choiceband"), "inventory"));
            if (stack.is(CobblemonItems.FOCUS_BAND)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "focusband"), "inventory"));
            if (stack.is(CobblemonItems.MUSCLE_BAND)) return modelManager.getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "muscleband"), "inventory"));
        }

        return value;
    }
}