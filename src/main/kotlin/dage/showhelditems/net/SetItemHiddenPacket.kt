package dage.showhelditems.net

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.util.readUUID
import com.cobblemon.mod.common.util.writeUUID
import dage.showhelditems.ShowHeldItems
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import java.util.*

class SetItemHiddenPacket(val pokemonUUID: UUID, val isItemHidden: Boolean) : NetworkPacket<SetItemHiddenPacket> {
    override val id: Identifier = ID
    override fun encode(buffer: RegistryByteBuf) {
        buffer.writeUUID(pokemonUUID)
        buffer.writeBoolean(isItemHidden)
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id<CustomPayload>(ID);
    }

    companion object {
        val ID: Identifier = Identifier.of(ShowHeldItems.MOD_ID,"set_hidden_item")
        fun decode(buffer: RegistryByteBuf) = SetItemHiddenPacket(buffer.readUUID(), buffer.readBoolean())
    }
}