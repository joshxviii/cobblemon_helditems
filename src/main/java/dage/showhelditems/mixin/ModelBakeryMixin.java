package dage.showhelditems.mixin;

import com.cobblemon.mod.relocations.oracle.truffle.api.profiles.Profile;
import dage.showhelditems.ShowHeldItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.*;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Josh
 */
@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    protected abstract void loadItemModelAndDependencies(ResourceLocation modelId);

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Shadow abstract UnbakedModel getModel(ResourceLocation id);

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void addAccessoryModels(BlockColors blockColors, ProfilerFiller profiler, Map<ResourceLocation, BlockModel> jsonUnbakedModels, Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStates, CallbackInfo ci) {

        profiler.push("data.show-held-items.tag.item");

        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "blackglasses"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "choicespecs"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "safetygoggles"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "wiseglasses"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "rockyhelmet"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "kingsrock"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "expshare"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "choiceband"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "focusband"), "inventory").id());
        this.loadItemModelAndDependencies(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID, "muscleband"), "inventory").id());

        this.topLevelModels.values().forEach((model) -> {
            model.resolveParents(this::getModel);
        });

        profiler.pop();
    }
}
