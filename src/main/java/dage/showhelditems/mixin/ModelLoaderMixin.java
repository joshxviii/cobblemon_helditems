package dage.showhelditems.mixin;

import dage.showhelditems.ShowHeldItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
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
@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    @Shadow
    protected abstract void loadItemModel(ModelIdentifier modelId);

    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;

    @Shadow public abstract UnbakedModel getOrLoadModel(Identifier id);

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void addAccessoryModels(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SpriteGetter>> blockStates, CallbackInfo ci) {

        profiler.push("item");

        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "blackglasses"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "choicespecs"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "safetygoggles"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "wiseglasses"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "rockyhelmet"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "kingsrock"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "expshare"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "choiceband"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "focusband"), "inventory"));
        this.loadItemModel(new ModelIdentifier(Identifier.of(ShowHeldItems.MOD_ID, "muscleband"), "inventory"));

        this.modelsToBake.values().forEach((model) -> {
            model.setParents(this::getOrLoadModel);
        });

        profiler.pop();
    }
}
