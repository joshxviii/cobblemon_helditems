package dage.showhelditems.net

import com.cobblemon.mod.common.CobblemonNetwork.sendPacket
import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.api.storage.PokemonStore
import com.cobblemon.mod.common.util.party
import dage.showhelditems.ItemHiddenTracker
import dage.showhelditems.ItemVisibilityChangedEvent
import dage.showhelditems.ShowHeldItems
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object SetItemHiddenHandler : ServerNetworkPacketHandler<SetItemHiddenPacket> {

    override fun handle(packet: SetItemHiddenPacket, server: MinecraftServer, player: ServerPlayer) {

        val pokemonStore: PokemonStore<*> = player.party()
        val pokemon = pokemonStore[packet.pokemonUUID] ?: return

        val isItemHidden = packet.isItemHidden

        // Send an update that visibility has changed
        // Used to fix the issue with client side not updating
        ShowHeldItems.ITEM_VISIBILITY_CHANGED.postThen(
            event = ItemVisibilityChangedEvent(
                pokemon,
                isItemHidden
            ),
            ifSucceeded = {
                (pokemon as ItemHiddenTracker).isItemHidden = isItemHidden
            },
            ifCanceled = {
                return player.sendPacket(ItemHiddenUpdatePacket({ pokemon }, isItemHidden))
                //Use to undo update //return player.sendPacket(ItemHiddenUpdatePacket({ pokemon }, (pokemon as ItemHiddenTracker).isItemHidden))
            }
        )
    }
}