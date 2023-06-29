package com.ebicep.warlordsplusplus

import com.mojang.brigadier.Command
import net.minecraft.commands.Commands
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

const val MODID = "warlordsplusplus"

@Mod(MODID)
object WarlordsPlusPlus {

    val LOGGER: Logger = LogManager.getLogger(MODID)

    init {
        LOGGER.log(Level.INFO, "$MODID has started!")
        MOD_BUS.addListener(::onClientSetup)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    @SubscribeEvent
    fun registerCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registering commands")
        event.dispatcher.register(
            Commands.literal("test")
                .executes {

                    Command.SINGLE_SUCCESS
                }
        )
    }

}