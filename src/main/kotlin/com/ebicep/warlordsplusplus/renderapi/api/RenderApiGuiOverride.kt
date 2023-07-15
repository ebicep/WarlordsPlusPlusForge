package com.ebicep.warlordsplusplus.renderapi.api

import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay

abstract class RenderApiGuiOverride(private val toOverride: VanillaGuiOverlay) : RenderApiGui() {

    override fun shouldRender(event: RenderGuiOverlayEvent.Pre): Boolean {
        return event.overlay == toOverride.type()
    }

    override fun setUpRender(e: RenderGuiOverlayEvent.Pre) {
        e.isCanceled = true
        event = e
        poseStack = e.guiGraphics.pose()

        poseStack {
            render(e)
        }
    }

}