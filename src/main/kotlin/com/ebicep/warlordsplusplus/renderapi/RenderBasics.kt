package com.ebicep.warlordsplusplus.renderapi

import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import net.minecraftforge.eventbus.api.Event

interface RenderBasics<E : Event> {

    fun shouldRender(event: E) = true

    fun render(event: E)

    fun onRenderEvent(event: E) {
        if (!WarlordsPlusPlus.isEnabled()) {
            return
        }
        if (shouldRender(event)) {
            setUpRender(event)
        }
    }

    fun setUpRender(e: E)


}
