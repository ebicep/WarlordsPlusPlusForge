package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.MODID
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

interface ChatParser {

    fun onChatReceived(e: ClientChatReceivedEvent.System) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        private val parsers = mutableListOf<ChatParser>()

        init {
            parsers.add(DamageAndHealParser)
            parsers.add(KillAssistParser)
            parsers.add(HitDetector)
            parsers.add(GameEndDetector)
        }

        @SubscribeEvent
        fun onChatMessage(e: ClientChatReceivedEvent.System) {
            parsers.forEach {
                it.onChatReceived(e)
            }
        }

    }
}