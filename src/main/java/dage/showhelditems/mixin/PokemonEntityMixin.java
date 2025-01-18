package dage.showhelditems.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
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

    @Unique
    private static final TrackedData<ItemStack> HELD_ITEM;
    static {
        HELD_ITEM = DataTracker.registerData(PokemonEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTrackerInject(DataTracker.Builder builder, CallbackInfo ci) {
        ItemStack item = ItemStack.EMPTY;
        Pokemon p = ((PokemonEntity) (Object) this).getPokemon();
        if (p!=null ) item = p.heldItem();
        builder.add(HELD_ITEM, item);
    }

    @Override
    public @NotNull ItemStack getShownItem() {
        return ((PokemonEntity) (Object) this).getDataTracker().get(HELD_ITEM);
    }

    @Override
    public void setShownItem(@NotNull ItemStack itemStack) {
        ((PokemonEntity) (Object) this).getDataTracker().set(HELD_ITEM, itemStack);
    }
}
