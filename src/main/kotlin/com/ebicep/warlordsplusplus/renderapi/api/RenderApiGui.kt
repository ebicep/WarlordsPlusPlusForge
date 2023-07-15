package com.ebicep.warlordsplusplus.renderapi.api

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level

abstract class RenderApiGui(val autoRotate: Boolean = true) : RenderApi<RenderGuiOverlayEvent>() {

    override fun getMultiBufferSource(event: RenderGuiOverlayEvent): MultiBufferSource.BufferSource {
        return buffer //TODO ???
    }

    override fun setUpRender(e: RenderGuiOverlayEvent) {

    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        val buffer
            get() = Minecraft.getInstance().renderBuffers().bufferSource() //TODO maybe custom buffer?
        val toRender = mutableListOf<RenderApiGui>()

        init {
            WarlordsPlusPlus.LOGGER.log(
                Level.INFO,
                "RenderApiGui registered ${toRender.size} renderers: ${toRender.joinToString { it::class.simpleName!! }}"
            )
        }

        @SubscribeEvent
        fun onWorldRender(event: RenderGuiOverlayEvent) {
            toRender.forEach {
                it.onRenderEvent(event)
            }
        }

    }


}