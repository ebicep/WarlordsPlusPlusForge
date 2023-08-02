package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object DetectorEventHandler {

    private val chatParsers = mutableListOf<ChatParser>()
    private val resets = mutableListOf<Resets>()

    init {
        chatParsers.add(DamageAndHealParser)
        chatParsers.add(KillAssistParser)
        chatParsers.add(HitDetector)
        chatParsers.add(GameEndDetector)
    }

    @SubscribeEvent
    fun onChatMessage(e: ClientChatReceivedEvent.System) {
        chatParsers.forEach {
            it.onChatReceived(e)
        }
    }

    @SubscribeEvent
    fun onReset(e: WarlordsGameEvents.ResetEvent) {
        resets.forEach {
            it.onReset(e)
        }
    }
}