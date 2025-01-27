package dage.showhelditems.net

import com.cobblemon.mod.common.api.net.NetworkPacket
import dage.showhelditems.ShowHeldItems
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import java.util.*

/**
 * Sets a value for this pokemon that is saved for all players
 */
class SetItemHiddenPacket(val pokemonUUID: UUID, val isItemHidden: Boolean) :NetworkPacket<SetItemHiddenPacket> {
    override val id = ID
    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeUUID(pokemonUUID)
        buffer.writeBoolean(isItemHidden)
    }
    companion object {
        val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID,"set_hidden_item")
        fun decode(buffer: RegistryFriendlyByteBuf) = SetItemHiddenPacket(
            buffer.readUUID(), buffer.readBoolean()
        )
    }
}