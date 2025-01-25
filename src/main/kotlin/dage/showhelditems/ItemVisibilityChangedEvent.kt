package dage.showhelditems

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.pokemon.Pokemon

class ItemVisibilityChangedEvent(val pokemon: Pokemon, var isItemHidden: Boolean): Cancelable() {
}