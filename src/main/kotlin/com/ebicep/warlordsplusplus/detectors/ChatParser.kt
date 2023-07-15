package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.MODID
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

interface ChatParser {

    fun onChatReceived(e: ClientChatReceivedEvent) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        private val parsers = mutableListOf<ChatParser>()

        init {
            parsers.add(DamageAndHealParser)
            parsers.add(KillAssistParser)
            parsers.add(HitDetector)
        }

        @SubscribeEvent
        fun onChatMessage(e: ClientChatReceivedEvent) {
            if (!e.isSystem) {
                return
            }
            parsers.forEach {
                it.onChatReceived(e)
            }
        }

    }
}