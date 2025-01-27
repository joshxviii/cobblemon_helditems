package dage.showhelditems

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents.HELD_ITEM_POST
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_SENT_POST
import com.cobblemon.mod.common.api.events.pokemon.HeldItemEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonSentPostEvent
import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.client.CobblemonClient.storage
import com.cobblemon.mod.common.client.storage.ClientPC
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ShowHeldItems : ModInitializer {
	const val MOD_ID = "show-held-items"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	@JvmField
	val ITEM_VISIBILITY_CHANGED = CancelableObservable<ItemVisibilityChangedEvent>()

	@JvmField val WEARABLE_EYE_ITEMS: TagKey<Item> = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "wearable_eye_items"))
	@JvmField val WEARABLE_HAT_ITEMS: TagKey<Item> = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "wearable_hat_items"))
	@JvmField val HIDDEN_ITEMS: TagKey<Item> = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hidden_items"))
	/**
	 * Updates The HELD_ITEM DataTracker for a PokemonEntity.
	 * If the item is in the hidden list
	 */
	private fun updateShownItem(pokemonEntity: PokemonEntity, item: ItemStack, itemHidden: Boolean) {
		var shownItem = ItemStack.EMPTY;
		if (!item.`is`(HIDDEN_ITEMS)) shownItem = item;

		if (pokemonEntity is ShownItemTracker) {
			(pokemonEntity as ShownItemTracker).shownItem = shownItem
			(pokemonEntity as ShownItemTracker).isItemHidden = itemHidden
			//println("Update [ ITEM: $shownItem, IS_HIDDEN: $itemHidden ]")
		}
	}

	//Todo (Client only) when changing pokemon's held item visibility wont updated until sent out again
	/**
	 * @param pokemonEntity The pokemon to get the held item from.
	 * @return Returns the held item for this pokemon.
	 */
	fun getHeldItem(pokemonEntity: PokemonEntity): ItemStack? {

		//Hidden Check
		if((pokemonEntity as ShownItemTracker).isItemHidden) return ItemStack.EMPTY
		//Server Search
		if(!(pokemonEntity as ShownItemTracker).shownItem.isEmpty) return (pokemonEntity as ShownItemTracker).shownItem
		//Client Search
//		if (pokemonEntity.ownerUUID?.equals(Minecraft.getInstance().player?.uuid) == true) {
//			val storage = storage
//			val myParty = storage.myParty
//			val pcs: Collection<ClientPC> = storage.pcStores.values
//			//See if the pokemon that is being rendered is part of client users party
//			for (p in myParty) {
//				return comparePokemonEntity(p, pokemonEntity) ?: continue
//			}
//			//If not then check PC **this is from pokemon roaming around from the pasture block**
//			for (pc in pcs) for (box in pc.boxes) for (p in box.slots) {
//				return comparePokemonEntity(p, pokemonEntity) ?: continue
//			}
//		}
		//Default
		return ItemStack.EMPTY
	}

	private fun comparePokemonEntity(p: Pokemon?, pokemonEntity: PokemonEntity ) : ItemStack? {
		if (p == null) return null
		if (p.entity?.uuid?.equals(pokemonEntity.uuid) == true ) {
			if ((p as ItemHiddenTracker).isItemHidden) return ItemStack.EMPTY
			return p.heldItem()
		}
		return null
	}

	override fun onInitialize() {
		HELD_ITEM_POST.subscribe(Priority.NORMAL) { post: HeldItemEvent.Post ->
			val pokemonEntity: PokemonEntity? = post.pokemon.entity
			if (pokemonEntity != null) {
				updateShownItem(pokemonEntity, post.received, (post.pokemon as ItemHiddenTracker).isItemHidden)
			}
		}
		ITEM_VISIBILITY_CHANGED.subscribe(Priority.NORMAL) { event: ItemVisibilityChangedEvent ->
			val pokemonEntity: PokemonEntity? = event.pokemon.entity
			if (pokemonEntity != null) {
				updateShownItem(pokemonEntity, event.pokemon.heldItem(), event.isItemHidden)
			}
		}
		POKEMON_SENT_POST.subscribe(Priority.NORMAL) { post: PokemonSentPostEvent ->
			updateShownItem(post.pokemonEntity, post.pokemon.heldItem(), (post.pokemon as ItemHiddenTracker).isItemHidden)
		}
		EntityTrackingEvents.START_TRACKING.register(EntityTrackingEvents.StartTracking { target: Entity, player: ServerPlayer ->
			if (target is PokemonEntity) {
				updateShownItem(target, target.pokemon.heldItem(), (target.pokemon as ItemHiddenTracker).isItemHidden)
			}
		})
	}
}