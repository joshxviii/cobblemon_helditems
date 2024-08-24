
package dage.visualhelditems.mixin.client;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.render.MatrixWrapper;
import com.cobblemon.mod.common.client.render.models.blockbench.*;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.client.storage.ClientBox;
import com.cobblemon.mod.common.client.storage.ClientPC;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.client.storage.ClientStorageManager;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.visualhelditems.CobblemonHeldItemsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = PokemonRenderer.class)
abstract class PokemonRendererMixin {

    @Unique
    private final HeldItemRenderer heldItemRenderer = new HeldItemRenderer(MinecraftClient.getInstance(), MinecraftClient.getInstance().getEntityRenderDispatcher(), MinecraftClient.getInstance().getItemRenderer());

    @Inject(method = "render*", at = @At(value = "TAIL"))
    public void render(PokemonEntity entity, float entityYaw, float partialTicks, MatrixStack poseMatrix, VertexConsumerProvider buffer, int packedLight, CallbackInfo ci) {

        ItemStack heldItem = getHeldItem(entity.getUuid());

        if (!heldItem.isEmpty()) {
            PokemonPoseableModel model = PokemonModelRepository.INSTANCE.getPoser(entity.getPokemon().getSpecies().resourceIdentifier, entity.getAspects());
            Map<String, MatrixWrapper> locators = ((PoseableEntityModel<PokemonEntity>) model).getState(entity).getLocatorStates();

            poseMatrix.push();
            if (locators.containsKey("held_item")) {//render item the same way as the player
                poseMatrix.multiplyPositionMatrix( locators.get("held_item").getMatrix() );

                poseMatrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
                poseMatrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, poseMatrix, buffer, packedLight);
            }
            else if (locators.containsKey("held_item_fixed")) {//render flat ground item model
                poseMatrix.multiplyPositionMatrix( locators.get("held_item_fixed").getMatrix() );

                this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.GROUND, false, poseMatrix, buffer, packedLight);
            }
            poseMatrix.pop();
        }
    }

    @Unique
    private static ItemStack getHeldItem(UUID pokemonID) {
        //TODO Make the client storage search not so insanely bad..

        //check server item cache
        HashMap<UUID, ItemStack> cache = CobblemonHeldItemsClient.cachedHeldItems;
        if (cache.containsKey(pokemonID)) {
            return cache.get(pokemonID);
        }
        //if not in cache check client storage
        else {
            ClientStorageManager storage = CobblemonClient.INSTANCE.getStorage();
            ClientParty party = storage.getMyParty();
            Collection<ClientPC> pcs = storage.getPcStores().values();
            //See if the pokemon that is being rendered is part of client users party
            for (Pokemon p : party) {
                if (p == null) continue;
                PokemonEntity partyEntity = p.getEntity();
                if (partyEntity == null) continue;
                if (partyEntity.getUuid() == pokemonID) return p.heldItem();
            }
            //If not then check PC **this is from pokemon roaming around from the pasture block**
            //AKA triple for-loop nightmare
            for (ClientPC pc : pcs) {
                for (ClientBox box : pc.getBoxes()) {
                    for (Pokemon p : box.getSlots()) {
                        if (p == null) continue;
                        PokemonEntity partyEntity = p.getEntity();
                        if (partyEntity == null) continue;
                        if (partyEntity.getUuid() == pokemonID) return p.heldItem();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
}

