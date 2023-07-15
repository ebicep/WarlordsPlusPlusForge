package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.MODID
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

abstract class ChatParser {

    open fun onChatReceived(e: ClientChatReceivedEvent) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        private val parsers = mutableListOf<ChatParser>()

        init {
            parsers.add(DamageAndHealParser)
            parsers.add(KillAssistParser)
        }

        @SubscribeEvent
        fun onChatMessage(e: ClientChatReceivedEvent) {
            parsers.forEach {
                it.onChatReceived(e)
            }
        }

    }
}