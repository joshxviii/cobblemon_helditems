package com.dage.visualhelditems.mixin.client;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Josh
 */

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessorMixin {
    @Accessor("models")
    ItemModels item$getModels();
}
