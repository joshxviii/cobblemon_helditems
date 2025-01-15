package dev.dage.showhelditems

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.mixin.registry.sync.RegistryKeysMixin
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory



object ShowHeldItems : ModInitializer {
	const val MOD_ID = "show-held-items"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	val WEARABLE_HAT_ITEMS: TagKey<Item> = TagKey.of(Registry., Identifier.tryParse(MOD_ID, "wearable_hat_items"))
	val HIDDEN_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "hidden_items"))

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!")
	}
}