package dage.showhelditems.net

import com.cobblemon.mod.common.net.messages.client.pokemon.update.SingleUpdatePacket
import com.cobblemon.mod.common.pokemon.Pokemon
import dage.showhelditems.ItemHiddenTracker
import dage.showhelditems.ShowHeldItems
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

class ItemHiddenUpdatePacket(pokemon: () -> Pokemon, value: Boolean) : SingleUpdatePacket<Boolean, ItemHiddenUpdatePacket>(pokemon, value) {
    override val id: Identifier = ID
    override fun encodeValue(buffer: RegistryByteBuf) {
        buffer.writeBoolean(value)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id<CustomPayload>(ID);
    }

    override fun set(pokemon: Pokemon, value: Boolean) {
        (pokemon as ItemHiddenTracker).isItemHidden = value
    }

    companion object {
        val ID: Identifier = Identifier.of(ShowHeldItems.MOD_ID,"hidden_item_update")
        fun decode(buffer: RegistryByteBuf): ItemHiddenUpdatePacket {
            val pokemon = decodePokemon(buffer)
            val isItemHidden = buffer.readBoolean()
            return ItemHiddenUpdatePacket(pokemon, isItemHidden)
        }
    }
}