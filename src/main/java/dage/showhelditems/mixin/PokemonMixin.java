package dage.showhelditems.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.api.storage.StoreCoordinates;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.PokemonUpdatePacket;
import com.cobblemon.mod.common.net.messages.client.pokemon.update.FormUpdatePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.net.ItemHiddenUpdatePacket;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;

/**
 * @author Josh
 */
@Mixin(value = Pokemon.class)
public abstract class PokemonMixin implements ItemHiddenTracker {


    @Shadow public abstract void notify(@NotNull PokemonUpdatePacket<?> packet);

    @Unique
    private Boolean isItemHidden = false;

    @Unique
    private final SimpleObservable<Boolean> _isItemHidden = registerObservable(new SimpleObservable<>(), it -> new ItemHiddenUpdatePacket(() -> ((Pokemon) (Object) this), it));

    @Override
    public boolean isItemHidden() {
        if (isItemHidden!=null) return isItemHidden;
        return false;
    }

    @Override
    public void setItemHidden(boolean b) {
        isItemHidden = b;
        _isItemHidden.emit(b);
    }

    @Unique
    private <T> SimpleObservable<T> registerObservable( SimpleObservable<T> observable, Function<T, PokemonUpdatePacket<?>> notifyPacket) {
        observable.subscribe(Priority.HIGH,it ->
        {
            PokemonUpdatePacket<?> packet = notifyPacket.apply(it);

            StoreCoordinates<?> storeCoords = ((Pokemon) (Object) this).getStoreCoordinates().get();
            if (storeCoords!=null){
                if (packet != null) CobblemonNetwork.INSTANCE.sendPacketToPlayers(storeCoords.getStore().getObservingPlayers(), packet );
            }
            return null;
        });
        return observable;
    }

}
