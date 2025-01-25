package dage.showhelditems

import net.minecraft.item.ItemStack

/**
 * Used to inject a HELD_ITEM DataTracker into a PokemonEntity.
 */
interface ShownItemTracker {
    var shownItem: ItemStack
    var isItemHidden: Boolean
}