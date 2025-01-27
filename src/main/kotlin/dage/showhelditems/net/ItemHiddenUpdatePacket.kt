package dage.showhelditems.net

import com.cobblemon.mod.common.net.messages.client.pokemon.update.SingleUpdatePacket
import com.cobblemon.mod.common.pokemon.Pokemon
import dage.showhelditems.ItemHiddenTracker
import dage.showhelditems.ShowHeldItems
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation

class ItemHiddenUpdatePacket(pokemon: () -> Pokemon, value: Boolean) : SingleUpdatePacket<Boolean, ItemHiddenUpdatePacket>(pokemon, value) {
    override val id: ResourceLocation = ID
    override fun encodeValue(buffer: RegistryFriendlyByteBuf) {
        buffer.writeBoolean(this.value)
    }

    override fun set(pokemon: Pokemon, value: Boolean) {
        (pokemon as ItemHiddenTracker).isItemHidden = this.value
    }

    companion object {
        val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID,"hidden_item_update")
        fun decode(buffer: RegistryFriendlyByteBuf): ItemHiddenUpdatePacket {
            val pokemon = decodePokemon(buffer)
            val isItemHidden = buffer.readBoolean()
            return ItemHiddenUpdatePacket(pokemon, isItemHidden)
        }
    }
}