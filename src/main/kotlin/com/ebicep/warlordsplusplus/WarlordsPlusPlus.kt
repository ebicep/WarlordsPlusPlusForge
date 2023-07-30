package com.ebicep.warlordsplusplus

import com.ebicep.warlordsplusplus.channel.WarlordsPvEPacketHandler
import com.ebicep.warlordsplusplus.config.Config
import com.ebicep.warlordsplusplus.config.ConfigGui
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import com.ebicep.warlordsplusplus.renderapi.api.RenderApiGui
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.RegisterClientCommandsEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.GENERAL_SPEC, "WarlordsPlusPlus.toml");
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
            ConfigScreenHandler.ConfigScreenFactory { _: Minecraft, screen: Screen ->
                ConfigGui(screen)
            }
        }
        MOD_BUS.addListener(this::onClientSetup)
        MOD_BUS.addListener(RenderApiGui::onGuiRegister)
        FORGE_BUS.addListener(this::onRegisterCommands)
    }

    fun isEnabled(): Boolean {
        return Config.enabled.get()
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
                        FORGE_BUS.post(WarlordsGameEvents.ResetEvent())
                        OtherWarlordsPlayers.getOtherWarlordsPlayers()
                        LOGGER.info(OtherWarlordsPlayers.playersMap)
                        1
                    }
            )
            .executes {
                Minecraft.getInstance().setScreen(ConfigGui(null))
                1
            })

    }

}