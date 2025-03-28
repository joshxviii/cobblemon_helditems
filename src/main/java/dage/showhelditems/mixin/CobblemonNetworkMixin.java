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
import dage.showhelditems.net.SetItemHiddenHandler;
import dage.showhelditems.net.SetItemHiddenPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * @author Josh
 */
@Mixin(value = CobblemonNetwork.class, remap = false)
public abstract class CobblemonNetworkMixin {

    @Inject(method = "generateS2CPacketInfoList", at = @At("RETURN"), cancellable = true)
    private void generateS2CPacketInfoList(CallbackInfoReturnable<List<PacketRegisterInfo<?>>> cir){
        List<PacketRegisterInfo<?>> list = cir.getReturnValue();

        list.add(new PacketRegisterInfo<>(ItemHiddenUpdatePacket.Companion.getID(), ItemHiddenUpdatePacket.Companion::decode, new PokemonUpdatePacketHandler<>(), null));

        cir.setReturnValue(list);
    }

    /**
     * Register my packet in the c2s map.
     */
    @Inject(method = "generateC2SPacketInfoList", at = @At("RETURN"), cancellable = true)
    private void generateC2SPacketInfoList(CallbackInfoReturnable<List<PacketRegisterInfo<?>>> cir){
        List<PacketRegisterInfo<?>> list = cir.getReturnValue();

        list.add(new PacketRegisterInfo<>(SetItemHiddenPacket.Companion.getID(), SetItemHiddenPacket.Companion::decode, new SetItemHiddenHandler(), null));

        cir.setReturnValue(list);
    }
}
