package com.ebicep.warlordsplusplus.renderapi.api

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
import org.lwjgl.opengl.GL11

abstract class RenderApiGuiOverride(private val toOverride: VanillaGuiOverlay) : RenderApiGui() {

    override fun shouldRender(event: RenderGuiOverlayEvent.Pre): Boolean {
        return event.overlay == toOverride.type()
    }

    override fun setUpRender(e: RenderGuiOverlayEvent.Pre) {
        e.isCanceled = true
        event = e
        poseStack = e.guiGraphics.pose()

        poseStack {
            RenderSystem.depthMask(true)
            RenderSystem.enableDepthTest()
            RenderSystem.enableBlend()
            RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

            render(e)

            RenderSystem.depthMask(false)
            RenderSystem.disableDepthTest()
            RenderSystem.disableBlend()
            RenderSystem.defaultBlendFunc()
        }
    }

}