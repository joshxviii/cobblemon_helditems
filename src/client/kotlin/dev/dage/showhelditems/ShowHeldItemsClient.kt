package dev.dage.showhelditems

import net.fabricmc.api.ClientModInitializer
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ShowHeldItemsClient : ClientModInitializer {
	const val MOD_ID = "show-held-items"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	val WEARABLE_EYE_ITEMS: TagKey<Item> = TagKey.create(ResourceKey.)
	val WEARABLE_HAT_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "wearable_hat_items"))
	val HIDDEN_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "hidden_items"))


	override fun onInitializeClient() {

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}