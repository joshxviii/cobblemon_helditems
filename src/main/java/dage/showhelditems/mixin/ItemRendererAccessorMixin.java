package dage.showhelditems.mixin;


import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Josh
 */

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessorMixin {
    @Accessor("itemModelShaper")
    ItemModelShaper item$getModels();
}
