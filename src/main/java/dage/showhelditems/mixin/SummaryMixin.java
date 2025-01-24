package dage.showhelditems.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.scheduling.SchedulingTracker;
import com.cobblemon.mod.common.client.gui.summary.Summary;
import com.cobblemon.mod.common.client.gui.summary.SummaryButton;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dage.showhelditems.ItemHiddenTracker;
import dage.showhelditems.ShowHeldItems;
import dage.showhelditems.net.SetItemHiddenPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

/**
 * @author Josh
 */
@Mixin(value = Summary.class)
public abstract class SummaryMixin extends Screen{

    @Unique
    SchedulingTracker schedulingTracker = new SchedulingTracker();

    @Unique
    private static final int BASE_WIDTH = 331;
    @Unique
    private static final int BASE_HEIGHT = 161;
    @Unique
    private static final Identifier visibleResource = Identifier.of(ShowHeldItems.MOD_ID,"textures/gui/item_visible.png");
    @Unique
    private static final Identifier hiddenResource = Identifier.of(ShowHeldItems.MOD_ID,"textures/gui/item_hidden.png");

    @Shadow
    public Pokemon selectedPokemon;

    @Shadow public abstract void playSound(@NotNull SoundEvent soundEvent);

    protected SummaryMixin(Text title) {
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
                Text.literal(""),
                visibleResource,
                visibleResource,
                button -> (!selectedPokemon.heldItem().isEmpty()) && !((ItemHiddenTracker) selectedPokemon).isItemHidden(),
                button -> (!selectedPokemon.heldItem().isEmpty()),
                true,
                true,
                true,
                true,
                1F
        );
        SummaryButton showHeldItemBtn = new SummaryButton(
                btnX, btnY, btnWidth, btnHeight,
                button -> {},
                Text.literal(""),
                hiddenResource,
                hiddenResource,
                button -> (!selectedPokemon.heldItem().isEmpty()) && ((ItemHiddenTracker) selectedPokemon).isItemHidden(),
                button -> false,
                true,
                true,
                true,
                true,
                1F
        );
        addDrawableChild(showHeldItemBtn);
        addDrawableChild(hideHeldItemBtn);
    }

    @Unique
    private void onHideItemPress(){
        if (selectedPokemon instanceof ItemHiddenTracker) {
            boolean value = !((ItemHiddenTracker) selectedPokemon).isItemHidden();
            ((ItemHiddenTracker) selectedPokemon).setItemHidden( value );
            this.playSound(CobblemonSounds.GUI_CLICK);

            CobblemonNetwork.INSTANCE.sendToServer(
                new SetItemHiddenPacket(
                    selectedPokemon.getUuid(),
                    ((ItemHiddenTracker) selectedPokemon).isItemHidden()
                )
            );
        }
    }
}
