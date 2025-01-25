package dage.showhelditems.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.ShownItemTracker;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
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

    @Unique private static final TrackedData<ItemStack> HELD_ITEM;
    @Unique private static final TrackedData<Boolean> IS_ITEM_HIDDEN;
    static {
        HELD_ITEM = DataTracker.registerData(PokemonEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        IS_ITEM_HIDDEN = DataTracker.registerData(PokemonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    protected void initDataTrackerInject(DataTracker.Builder builder, CallbackInfo ci) {
        ItemStack item = ItemStack.EMPTY;
        Boolean isItemHidden = false;
        Pokemon p = ((PokemonEntity) (Object) this).getPokemon();
        if (p!=null ) {
            item = p.heldItem();
            isItemHidden = ((ItemHiddenTracker)p).isItemHidden();
        }
        builder.add(HELD_ITEM, item);
        builder.add(IS_ITEM_HIDDEN, isItemHidden);
    }

    @Override
    public @NotNull ItemStack getShownItem() {
        return ((PokemonEntity) (Object) this).getDataTracker().get(HELD_ITEM);
    }

    @Override
    public void setShownItem(@NotNull ItemStack itemStack) {
        ((PokemonEntity) (Object) this).getDataTracker().set(HELD_ITEM, itemStack);
    }

    @Override
    public boolean isItemHidden() {
        return ((PokemonEntity) (Object) this).getDataTracker().get(IS_ITEM_HIDDEN);
    }

    @Override
    public void setItemHidden(boolean b) {
        ((PokemonEntity) (Object) this).getDataTracker().set(IS_ITEM_HIDDEN, b);
    }
}
