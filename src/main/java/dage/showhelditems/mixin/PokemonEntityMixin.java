package dage.showhelditems.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.ShownItemTracker;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Josh
 */
@Mixin(PokemonEntity.class)
public class PokemonEntityMixin implements ShownItemTracker {

    @Unique private static final EntityDataAccessor<ItemStack> HELD_ITEM;
    @Unique private static final EntityDataAccessor<Boolean> IS_ITEM_HIDDEN;
    static {
        HELD_ITEM = SynchedEntityData.defineId(PokemonEntity.class, EntityDataSerializers.ITEM_STACK);
        IS_ITEM_HIDDEN = SynchedEntityData.defineId(PokemonEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    protected void initDataTrackerInject(SynchedEntityData.Builder builder, CallbackInfo ci) {
        ItemStack item = ItemStack.EMPTY;
        boolean isItemHidden = false;
        Pokemon p = ((PokemonEntity) (Object) this).getPokemon();
        if (p!=null ) {
            item = p.heldItem();
            isItemHidden = ((ItemHiddenTracker)p).isItemHidden();
        }
        builder.define(HELD_ITEM, item);
        builder.define(IS_ITEM_HIDDEN, isItemHidden);
    }

    @Override
    public @NotNull ItemStack getShownItem() {
        return ((PokemonEntity) (Object) this).getEntityData().get(HELD_ITEM);
    }

    @Override
    public void setShownItem(@NotNull ItemStack itemStack) {
        ((PokemonEntity) (Object) this).getEntityData().set(HELD_ITEM, itemStack);
    }

    @Override
    public boolean isItemHidden() {
        return ((PokemonEntity) (Object) this).getEntityData().get(IS_ITEM_HIDDEN);
    }

    @Override
    public void setItemHidden(boolean b) {
        ((PokemonEntity) (Object) this).getEntityData().set(IS_ITEM_HIDDEN, b);
    }
}
