package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.game.GameStateManager
import net.minecraftforge.client.event.ClientChatReceivedEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object HitDetector : ChatParser() {

    override fun onChatReceived(e: ClientChatReceivedEvent) {
        if (GameStateManager.notInGame) {
            return
        }
        val msg: String = e.message.string
        if (msg.contains("You hit")) {
            FORGE_BUS.post(
                WarlordsPlayerEvents.HitEvent(
                    msg.substring(msg.indexOf("hit ") + 4, msg.indexOf("for") - 1),
                    GameStateManager.minute
                )
            )
        }
    }

}