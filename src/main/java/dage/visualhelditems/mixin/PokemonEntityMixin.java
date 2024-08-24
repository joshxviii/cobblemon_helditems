package dage.visualhelditems.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Josh
 */
@Mixin(PokemonEntity.class)
public class PokemonEntityMixin {

    @Unique
    TrackedData<ItemStack> HELD_ITEM = DataTracker.registerData(PokemonEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lcom/cobblemon/mod/common/pokemon/Pokemon;Lnet/minecraft/entity/EntityType;)V", at = @At("TAIL"))
    public void init(World world, Pokemon pokemon, EntityType type, CallbackInfo ci) {

    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTrackerInject(CallbackInfo ci) {
        //((PokemonEntity) (Object) this).getDataTracker().startTracking(HELD_ITEM, ((PokemonEntity) (Object) this).getPokemon().heldItem());
    }

}
