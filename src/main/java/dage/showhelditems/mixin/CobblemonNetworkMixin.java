package dage.showhelditems.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler;
import com.cobblemon.mod.common.api.storage.PokemonStore;
import com.cobblemon.mod.common.client.net.pokemon.update.PokemonUpdatePacketHandler;
import com.cobblemon.mod.common.net.PacketRegisterInfo;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.ItemVisibilityChangedEvent;
import dage.showhelditems.ShowHeldItems;
import dage.showhelditems.net.ItemHiddenUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author Josh
 */
@Mixin(value = CobblemonNetwork.class)
public abstract class CobblemonNetworkMixin {

    @Inject(method = "generateS2CPacketInfoList", at = @At("RETURN"), cancellable = true, remap = false)
    private void generateS2CPacketInfoList(CallbackInfoReturnable<List<PacketRegisterInfo<?>>> cir){
        List<PacketRegisterInfo<?>> list = cir.getReturnValue();

        list.add(new PacketRegisterInfo<>(ItemHiddenUpdatePacket.Companion.getID(), ItemHiddenUpdatePacket.Companion::decode, new PokemonUpdatePacketHandler<>(), null));

        cir.setReturnValue(list);
    }

    /**
     * Register my packet in the c2s map.
     */
    @Inject(method = "generateC2SPacketInfoList", at = @At("RETURN"), cancellable = true, remap = false)
    private void generateC2SPacketInfoList(CallbackInfoReturnable<List<PacketRegisterInfo<?>>> cir){
//        List<PacketRegisterInfo<?>> list = cir.getReturnValue();
//
//        //Todo i don't think I need this anymore
//        ServerNetworkPacketHandler<SetItemHiddenPacket> SetHandler = (packet, minecraftServer, player) -> {
//            PokemonStore<?> pokemonStore = Cobblemon.INSTANCE.getStorage().getParty(player);
//
//            Pokemon pokemon =  pokemonStore.get(packet.getPokemonUUID());
//            if (pokemon==null) return;
//            ((ItemHiddenTracker)pokemon).setItemHidden(packet.isItemHidden());
//
//            ShowHeldItems.ITEM_VISIBILITY_CHANGED.postThen(
//                new ItemVisibilityChangedEvent(
//                    pokemon,
//                    packet.isItemHidden()
//                ),
//                s -> {
//                    ((ItemHiddenTracker) pokemon).setItemHidden( packet.isItemHidden() );
//                    return null;
//                },
//                c -> {
//                    //this.sendPacketToPlayer(player, new ItemHiddenUpdatePacket(() -> pokemon,((ItemHiddenTracker)pokemon).isItemHidden()));
//                    return null;
//                }
//            );
//
//        };
//
//        list.add(new PacketRegisterInfo<>(SetItemHiddenPacket.Companion.getID(), SetItemHiddenPacket.Companion::decode, SetHandler, null));
//        cir.setReturnValue(list);
    }

}
