package dage.showhelditems.mixin;

import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.models.blockbench.LocatorAccess;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableState;
import com.cobblemon.mod.common.entity.generic.GenericBedrockEntity;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Josh
 */
@Mixin(value = PosableModel.class)
public class PosableModelMixin {

    @Shadow
    public LocatorAccess locatorAccess;

    @Shadow private transient boolean isForLivingEntityRenderer;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public final void updateLocators(Entity entity, PosableState state) {

        PoseStack matrixStack = new PoseStack();
        float scale = 1F;

        switch (entity) {
            case null -> {
                return;
            }
            case PokemonEntity pokemonEntity -> {
                float yRot = Mth.lerp(state.getPartialTicks(), pokemonEntity.yBodyRotO, pokemonEntity.yBodyRot);
                matrixStack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
                matrixStack.pushPose();
                matrixStack.scale(-1F, -1F, 1F);
                scale = pokemonEntity.getPokemon().getForm().getBaseScale() * pokemonEntity.getPokemon().getScaleModifier() * ((PokemonClientDelegate) pokemonEntity.getDelegate()).getEntityScaleModifier();
                matrixStack.scale(scale, scale, scale);
            }
            case EmptyPokeBallEntity emptyPokeBallEntity -> {
                float yRot = Mth.lerp(state.getPartialTicks(), emptyPokeBallEntity.getYRot(), emptyPokeBallEntity.yRotO);
                matrixStack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
                matrixStack.pushPose();
                matrixStack.scale(1F, -1F, -1F);
                scale = 0.7F;
                matrixStack.scale(scale, scale, scale);
            }
            case GenericBedrockEntity genericBedrockEntity -> {
                float yRot = Mth.lerp(state.getPartialTicks(), genericBedrockEntity.getYRot(), genericBedrockEntity.yRotO);
                matrixStack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
                matrixStack.pushPose();
                matrixStack.scale(1F, -1F, 1F);
            }
            case NPCEntity npcEntity -> {
                float yRot = Mth.lerp(state.getPartialTicks(), npcEntity.yBodyRotO, npcEntity.yBodyRot);
                matrixStack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
                matrixStack.pushPose();
                matrixStack.scale(-1F, -1F, 1F);
            }
            default -> {
            }
        }

        if (isForLivingEntityRenderer) {
            matrixStack.translate(0.0,-1.5,0.0);
        }

        locatorAccess.update(matrixStack, entity, scale, state.getLocatorStates(), true);
    }
}
