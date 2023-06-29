package com.ebicep.warlordsplusplus.renderapi.test

import com.ebicep.warlordsplusplus.renderapi.api.RenderApiPlayer
import net.minecraftforge.client.event.RenderPlayerEvent

object RenderPlayerTest : RenderApiPlayer() {

    override fun render(event: RenderPlayerEvent.Post) {
        poseStack {
            translateY(20)
            "TESTTETETETE".drawCentered()
        }
    }

}