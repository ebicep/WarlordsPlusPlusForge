package com.ebicep.warlordsplusplus

import com.ebicep.warlordsplusplus.channel.WarlordsPvEPacketHandler
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraftforge.client.event.RegisterClientCommandsEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

const val MODID = "warlordsplusplus"

@Mod(MODID)
object WarlordsPlusPlus {

    val LOGGER: Logger = LogManager.getLogger(MODID)

    init {
        LOGGER.log(Level.INFO, "$MODID has started!")
        MOD_BUS.addListener(this::onClientSetup)
        FORGE_BUS.addListener(this::onRegisterCommands)
    }

    //FMLCommonSetupEvent -> FMLClientSetupEvent -> InterModEnqueueEvent -> InterModProcessEvent
    //@SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")

        WarlordsPvEPacketHandler

        LOGGER.log(Level.INFO, "Done initializing client")
    }

    fun onRegisterCommands(event: RegisterClientCommandsEvent) {
        event.dispatcher.register(Commands.literal("warlordsplusplus")
            .then(
                LiteralArgumentBuilder.literal<CommandSourceStack?>("test")
                    .executes {
                        OtherWarlordsPlayers.getOtherWarlordsPlayers()
                        LOGGER.info(OtherWarlordsPlayers.playersMap)
                        1
                    }
            )
            .executes {
                1
            })

    }

}