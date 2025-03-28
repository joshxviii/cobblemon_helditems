package dage.showhelditems.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.client.gui.summary.Summary;
import com.cobblemon.mod.common.client.gui.summary.SummaryButton;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.ShowHeldItems;
import dage.showhelditems.net.SetItemHiddenPacket;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Josh
 */
@Mixin(value = Summary.class)
public abstract class SummaryMixin extends Screen {

    @Unique
    private static final int BASE_WIDTH = 331;
    @Unique
    private static final int BASE_HEIGHT = 161;
    @Unique
    private static final ResourceLocation visibleResource = ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID,"textures/gui/item_visible.png");
    @Unique
    private static final ResourceLocation hiddenResource = ResourceLocation.fromNamespaceAndPath(ShowHeldItems.MOD_ID,"textures/gui/item_hidden.png");

    @Shadow public Pokemon selectedPokemon;

    protected SummaryMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    protected void init(CallbackInfo ci){
        int x = (width - BASE_WIDTH) / 2;
        int y = (height - BASE_HEIGHT) / 2;
        float btnX = x + 22F;
        float btnY = y + 103F;
        int btnWidth = 13;
        int btnHeight = 7;

        SummaryButton hideHeldItemBtn = new SummaryButton(
                btnX, btnY, btnWidth, btnHeight,
                button -> onHideItemPress(),
                Component.literal(""),
                visibleResource,
                visibleResource,
                button -> (!selectedPokemon.heldItem().isEmpty()) && Boolean.FALSE.equals(((ItemHiddenTracker) selectedPokemon).isItemHidden()),
                button -> (!selectedPokemon.heldItem().isEmpty()),
                true,
                false,
                true,
                true,
                1F
        );
        SummaryButton showHeldItemBtn = new SummaryButton(
                btnX, btnY, btnWidth, btnHeight,
                button -> {},
                Component.literal(""),
                hiddenResource,
                hiddenResource,
                button -> (!selectedPokemon.heldItem().isEmpty()) && Boolean.TRUE.equals(((ItemHiddenTracker) selectedPokemon).isItemHidden()),
                button -> false,
                true,
                true,
                true,
                true,
                1F
        );
        addRenderableWidget(showHeldItemBtn);
        addRenderableWidget(hideHeldItemBtn);
    }

    @Unique
    private void onHideItemPress(){

        boolean value = Boolean.FALSE.equals(((ItemHiddenTracker) selectedPokemon).isItemHidden());
        ((ItemHiddenTracker) selectedPokemon).setItemHidden( value );

        // Send update to server
        // Todo This might not be necessary
        CobblemonNetwork.INSTANCE.sendToServer(
            new SetItemHiddenPacket(
                selectedPokemon.getUuid(),
                value
            )
        );
    }
}
