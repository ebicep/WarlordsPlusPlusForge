package com.ebicep.warlordsplusplus.renderapi.test

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import net.minecraft.client.gui.GuiGraphics
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.client.gui.overlay.ForgeGui
import net.minecraftforge.client.gui.overlay.IGuiOverlay
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object CustomHud : IGuiOverlay {

    @SubscribeEvent
    fun onWorldRender(event: RegisterGuiOverlaysEvent) {
        event.registerAboveAll("test", CustomHud)
        WarlordsPlusPlus.LOGGER.log(
            Level.INFO,
            "Registered HudFpsCounter"
        )
    }

    override fun render(gui: ForgeGui, guiGraphics: GuiGraphics, partialTick: Float, screenWidth: Int, screenHeight: Int) {
        //guiGraphics.pose().pushPose()
        //guiGraphics.pose().scale(2f, 2f, 2f)
        //RenderSystem.setShader() { GameRenderer.getRendertypeTextShader()}
//        guiGraphics.drawString(
//            gui.font,
//            "Hello World! Hello World! Hello World! Hello World!",
//            screenWidth / 2,
//            screenHeight / 2,
//            0xFFFFFF,
//            false
//        )
        //guiGraphics.pose().popPose()
    }

}