package com.dage.visualhelditems

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents.HELD_ITEM_POST
import com.cobblemon.mod.common.api.events.pokemon.HeldItemEvent
import com.cobblemon.mod.common.client.CobblemonClient.storage
import com.cobblemon.mod.common.client.storage.ClientPC
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object CobblemonHeldItems {
    const val MOD_ID = "cobblemon_helditems"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    /**
     * A list of pokemon entities currently being tracked on the server.
     */
    val cachedTrackedPokemon: HashMap<UUID, ItemStack> = HashMap()

    val WEARABLE_EYE_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("cobblemon_helditems", "wearable_eye_items"))
    val WEARABLE_HAT_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("cobblemon_helditems", "wearable_hat_items"))
    val HIDDEN_ITEMS: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("cobblemon_helditems", "hidden_items"))

    fun updateShownItem(pokemonEntity :PokemonEntity, item: ItemStack) {
        var shownItem = ItemStack.EMPTY;
        if (!item.isIn(HIDDEN_ITEMS)) shownItem = item;

        if (pokemonEntity is ShownItemTracker) {
            (pokemonEntity as ShownItemTracker).shownItem = shownItem
        }
    }


    fun getHeldItem(pokemonEntity: PokemonEntity): ItemStack? {
        val pokemonID : UUID = pokemonEntity.uuid;
        //TODO Make the client storage search not so insanely bad..
        if (pokemonEntity.ownerUuid != MinecraftClient.getInstance().player?.uuid){
            if (pokemonEntity is ShownItemTracker) {
                val i = pokemonEntity.shownItem
                if (!i.isEmpty) return i
            }
        } else {
            val storage = storage
            val party = storage.myParty
            val pcs: Collection<ClientPC> = storage.pcStores.values
            //See if the pokemon that is being rendered is part of client users party
            for (p in party) {
                if (p == null) continue
                val partyEntity = p.entity ?: continue
                if (partyEntity.uuid === pokemonID) return p.heldItem()
            }
            //If not then check PC **this is from pokemon roaming around from the pasture block**
            //AKA triple for-loop nightmare
            for (pc in pcs) {
                for (box in pc.boxes) {
                    for (p in box.slots) {
                        if (p == null) continue
                        val partyEntity = p.entity ?: continue
                        if (partyEntity.uuid === pokemonID) return p.heldItem()
                    }
                }
            }
        }
        return ItemStack.EMPTY
    }

    fun initialize() {
        HELD_ITEM_POST.subscribe(Priority.NORMAL) { post: HeldItemEvent.Post ->
            val pokemonEntity: PokemonEntity? = post.pokemon.entity

            if (pokemonEntity != null) {
                updateShownItem(pokemonEntity, post.received)
            }
        }
    }
}