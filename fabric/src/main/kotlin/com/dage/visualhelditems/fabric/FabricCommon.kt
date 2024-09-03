package com.dage.visualhelditems.fabric

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.dage.visualhelditems.CobblemonHeldItems
import com.dage.visualhelditems.CobblemonHeldItems.LOGGER
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity

class FabricCommon : ModInitializer {
    override fun onInitialize() {
        CobblemonHeldItems.initialize()

        EntityTrackingEvents.START_TRACKING.register(EntityTrackingEvents.StartTracking { target: Entity, player: ServerPlayerEntity ->
            if (target is PokemonEntity) {
                CobblemonHeldItems.updateShownItem(target, target.pokemon.heldItem())
            }
        })
    }
}