package com.dage.visualhelditems.forge

import com.dage.visualhelditems.CobblemonHeldItems
import com.dage.visualhelditems.CobblemonHeldItems.MOD_ID
import com.dage.visualhelditems.forge.events.ForgeEvents
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod


@Mod(MOD_ID)
class ForgeLoader {

    init {
        CobblemonHeldItems.initialize()
        MinecraftForge.EVENT_BUS.register(this)
        ForgeEvents.register()
    }

}