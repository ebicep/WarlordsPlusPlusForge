package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object GameEndDetector : ChatParser {

    var divider = ""
    var dividerComponent: MutableComponent = Component.empty()
        get() = Component.literal(divider).withStyle {
            it.withColor(ChatFormatting.GREEN).withBold(true)
        }
    var canPost = false
    var counter = 0

    @SubscribeEvent
    fun onReset(event: WarlordsGameEvents.ResetEvent) {
        canPost = false
        counter = 0
    }

    override fun onChatReceived(e: ClientChatReceivedEvent.System) {
        val message = e.message
        val unformattedText = message.string

        if (unformattedText.contains("YOUR STATISTICS")) {
            canPost = true
            counter = 0
        }
        if (unformattedText.contains("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
            divider = unformattedText
            counter++
            if (counter > 0 && canPost) {
                FORGE_BUS.post(WarlordsGameEvents.GameEndEvent())
                canPost = false
            }
        }
    }

}