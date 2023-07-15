package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.game.GameStateManager
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object KillAssistParser : ChatParser {

    override fun onChatReceived(e: ClientChatReceivedEvent.System) {
        if (GameStateManager.notInGame) {
            return
        }
        try {
            val textMessage: String = e.message.string
            when {
                textMessage.contains("was killed by") -> {
                    val player = textMessage.substring(textMessage.indexOf("by") + 3)
                    val deathPlayer = textMessage.substring(0, textMessage.indexOf("was") - 1)
                    FORGE_BUS.post(WarlordsPlayerEvents.KillEvent(player, deathPlayer, GameStateManager.minute, 0))
                }

                textMessage.contains("You were killed") -> {
                    val player = textMessage.substring(textMessage.indexOf("by ") + 3)
                    val deathPlayer = Minecraft.getInstance().player!!.scoreboardName
                    FORGE_BUS.post(WarlordsPlayerEvents.KillEvent(player, deathPlayer, GameStateManager.minute, 0))
                }

                textMessage.contains("You killed") -> {
                    val deathPlayer = textMessage.substring(textMessage.indexOf("killed ") + 7)
                    val player = Minecraft.getInstance().player!!.scoreboardName
                    FORGE_BUS.post(WarlordsPlayerEvents.KillEvent(player, deathPlayer, GameStateManager.minute, 0))
                }

                textMessage.contains("You assisted") -> {
                    val playerThatStoleKill =
                        textMessage.substring(textMessage.indexOf("You assisted ") + 13, textMessage.indexOf("in ") - 1)
                    FORGE_BUS.post(WarlordsPlayerEvents.KillStealEvent(playerThatStoleKill))
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}
