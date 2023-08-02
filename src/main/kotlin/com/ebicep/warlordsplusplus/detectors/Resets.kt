package com.ebicep.warlordsplusplus.detectors

import com.ebicep.warlordsplusplus.events.WarlordsGameEvents

interface Resets {

    fun onReset(e: WarlordsGameEvents.ResetEvent)

}