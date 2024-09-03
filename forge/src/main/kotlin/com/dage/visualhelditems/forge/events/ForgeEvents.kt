package com.dage.visualhelditems.forge.events

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.dage.visualhelditems.CobblemonHeldItems
import com.dage.visualhelditems.CobblemonHeldItems.LOGGER
import com.dage.visualhelditems.CobblemonHeldItems.MOD_ID
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ForgeEvents {

    fun register() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onStartTracking(event : StartTracking) {
        val target = event.target
        if (target is PokemonEntity) {
            CobblemonHeldItems.updateShownItem(target, target.pokemon.heldItem())
        }
    }


}