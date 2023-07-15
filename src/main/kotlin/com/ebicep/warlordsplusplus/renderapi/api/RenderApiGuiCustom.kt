package com.ebicep.warlordsplusplus.renderapi.api

import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.gui.overlay.ForgeGui
import net.minecraftforge.client.gui.overlay.IGuiOverlay

abstract class RenderApiGuiCustom : RenderApiGui(), IGuiOverlay {


    override fun setUpRender(e: RenderGuiOverlayEvent.Pre) {
        if (e.overlay.overlay == this) {
            event = e
            poseStack = e.guiGraphics.pose()
        } else {
            event = null
            poseStack = null
        }
    }

    override fun render(event: RenderGuiOverlayEvent.Pre) {
        // use IGuiOverlay rendering
    }

    override fun render(gui: ForgeGui, guiGraphics: GuiGraphics, partialTick: Float, screenWidth: Int, screenHeight: Int) {
        if (event == null || poseStack == null) {
            return
        }
        poseStack {
            customRender(gui, guiGraphics, partialTick, screenWidth, screenHeight)
        }
    }

    abstract fun customRender(gui: ForgeGui, guiGraphics: GuiGraphics, partialTick: Float, screenWidth: Int, screenHeight: Int)

    abstract fun register(event: RegisterGuiOverlaysEvent)

}