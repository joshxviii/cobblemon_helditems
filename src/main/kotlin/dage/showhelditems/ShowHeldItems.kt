package dage.showhelditems

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents.HELD_ITEM_POST
import com.cobblemon.mod.common.api.events.pokemon.HeldItemEvent
import com.cobblemon.mod.common.client.CobblemonClient.storage
import com.cobblemon.mod.common.client.storage.ClientPC
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ShowHeldItems : ModInitializer {
	const val MOD_ID = "show-held-items"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	val WEARABLE_EYE_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "wearable_eye_items"))
	val WEARABLE_HAT_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "wearable_hat_items"))
	val HIDDEN_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.tryParse(MOD_ID, "hidden_items"))

	/**
	 * Updates The HELD_ITEM DataTracker for a PokemonEntity.
	 * If the item is in the hidden list
	 */
	private fun updateShownItem(pokemonEntity : PokemonEntity, item: ItemStack) {
		var shownItem = ItemStack.EMPTY;
		if (!item.isIn(HIDDEN_ITEMS)) shownItem = item;

		if ( pokemonEntity is ShownItemTracker) {
			(pokemonEntity as ShownItemTracker).shownItem = shownItem
		}
	}


	/*Todo Add a toggle next to the "give held item" button to hide the item.
	this could also be much more optimized when I figure out how to send packets.. ;_;*/
	/**
	 * @param pokemonEntity The pokemon to get the held item from.
	 * @return Returns the held item for this pokemon.
	 */
	fun getHeldItem(pokemonEntity: PokemonEntity): ItemStack? {

		//Client Search
		if (pokemonEntity.ownerUuid?.equals(MinecraftClient.getInstance().player?.uuid) == true) {
			val storage = storage
			val myParty = storage.myParty
			val pcs: Collection<ClientPC> = storage.pcStores.values
			//See if the pokemon that is being rendered is part of client users party
			for (p in myParty) {
				if (p == null) continue
				val partyEntity = p.entity ?: continue// Todo there is a bug with pasture pokemon not having the same uuid (might be on cobblemon's side)
				if (partyEntity.uuid.equals(pokemonEntity.uuid)) {
					if ((p as ItemHiddenTracker).isItemHidden) {
						(pokemonEntity.pokemon as ItemHiddenTracker).isItemHidden = (p as ItemHiddenTracker).isItemHidden
						return ItemStack.EMPTY
					}
					return p.heldItem()
				}
			}
			//If not then check PC **this is from pokemon roaming around from the pasture block**
			for (pc in pcs) {
				for (box in pc.boxes) {
					for (p in box.slots) {
						if (p == null) continue
						val partyEntity = p.entity ?: continue// Todo there is a bug with pasture pokemon not having the same uuid
						if (partyEntity.uuid.equals(pokemonEntity.uuid)) {
							if ((p as ItemHiddenTracker).isItemHidden) {
								(pokemonEntity.pokemon as ItemHiddenTracker).isItemHidden = (p as ItemHiddenTracker).isItemHidden
								return ItemStack.EMPTY
							}
							return p.heldItem()
						}
					}
				}
			}
		}
		//Hidden Check
		if((pokemonEntity.pokemon as ItemHiddenTracker).isItemHidden) {
			println("Yippee")
			return ItemStack.EMPTY
		}
		//Server Search
		if (pokemonEntity is ShownItemTracker) {
			val i = pokemonEntity.shownItem
			if (!i.isEmpty) return i
		}
		return ItemStack.EMPTY
	}

	override fun onInitialize() {
		HELD_ITEM_POST.subscribe(Priority.NORMAL) { post: HeldItemEvent.Post ->
			val pokemonEntity: PokemonEntity? = post.pokemon.entity

			if (pokemonEntity != null) {
				updateShownItem(pokemonEntity, post.received)
			}
		}
		EntityTrackingEvents.START_TRACKING.register(EntityTrackingEvents.StartTracking { target: Entity, player: ServerPlayerEntity ->
			if (target is PokemonEntity) {
				updateShownItem(target, target.pokemon.heldItem())
			}
		})
	}
}