package dage.visualhelditems.mixin.client;

import com.cobblemon.mod.common.CobblemonItems;
import dage.visualhelditems.CobblemonHeldItemsClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author Josh
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useItem(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (renderMode == ModelTransformationMode.HEAD) {
            BakedModelManager modelManager = ((ItemRenderer) (Object) this).getModels().getModelManager();

//            if (stack.isOf(CobblemonItems.BLACK_GLASSES)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "blackglasses", "inventory"));
//            if (stack.isOf(CobblemonItems.CHOICE_SPECS)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "choicespecs", "inventory"));
//            if (stack.isOf(CobblemonItems.SAFETY_GOGGLES)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "safetygoggles", "inventory"));
//            if (stack.isOf(CobblemonItems.WISE_GLASSES)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "wiseglasses", "inventory"));
//            if (stack.isOf(CobblemonItems.ROCKY_HELMET)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "rockyhelmet", "inventory"));
//            if (stack.isOf(CobblemonItems.EXP_SHARE)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "expshare", "inventory"));
//            if (stack.isOf(CobblemonItems.KINGS_ROCK)) return modelManager.getModel(new ModelIdentifier(CobblemonHeldItemsClient.MOD_ID, "kingsrock", "inventory"));
        }

        return value;
    }
}