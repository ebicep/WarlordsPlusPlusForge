package com.ebicep.warlordsplusplus.detectors

import net.minecraftforge.client.event.ClientChatReceivedEvent

interface ChatParser {

    fun onChatReceived(e: ClientChatReceivedEvent.System)

}