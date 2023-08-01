package com.ebicep.warlordsplusplus.renderapi.api

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.modules.scoreboard.WarlordsPlusPlusScoreBoard
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level

abstract class RenderApiGui : RenderApi<RenderGuiOverlayEvent.Pre>() {

    override fun getMultiBufferSource(event: RenderGuiOverlayEvent.Pre): MultiBufferSource.BufferSource {
        return buffer //TODO ???
    }

    override fun setUpRender(e: RenderGuiOverlayEvent.Pre) {
        event = e
        poseStack = e.guiGraphics.pose()
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        val width: Int
            get() = mc.window.guiScaledWidth
        val height: Int
            get() = mc.window.guiScaledHeight
        val xCenter: Int
            get() = width / 2
        val yCenter: Int
            get() = height / 2

        val buffer
            get() = Minecraft.getInstance().renderBuffers().bufferSource() //TODO maybe custom buffer?
        val toRender = mutableListOf<RenderApiGui>()

        init {
            toRender.add(WarlordsPlusPlusScoreBoard)
        }

        @SubscribeEvent
        fun onWorldRender(event: RenderGuiOverlayEvent.Pre) {
            toRender.forEach {
                it.onRenderEvent(event)
            }
        }

        fun onGuiRegister(event: RegisterGuiOverlaysEvent) {
            toRender.forEach {
                if (it is RenderApiGuiCustom) {
                    it.register(event)
                }
            }
            WarlordsPlusPlus.LOGGER.log(
                Level.INFO,
                "RenderApiGui registered ${toRender.size} renderers: ${toRender.joinToString { it::class.simpleName!! }}"
            )
        }

    }


}