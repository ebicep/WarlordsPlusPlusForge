package com.ebicep.warlordsplusplus.renderapi

import net.minecraftforge.eventbus.api.Event

interface RenderBasics<E : Event> {

    fun shouldRender(event: E) = true

    fun render(event: E)

    fun onRenderEvent(event: E) {
        if (shouldRender(event)) {
            setUpRender(event)
        }
    }

    fun setUpRender(e: E)


}
