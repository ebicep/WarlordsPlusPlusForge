package com.ebicep.warlordsplusplus.events

import net.minecraftforge.eventbus.api.Event
import java.time.Instant

class WarlordsGameEvents {

    data class ResetEvent(val time: Instant? = Instant.now()) : Event()

}