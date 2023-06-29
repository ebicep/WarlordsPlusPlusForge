package com.ebicep.warlordsplusplus.renderapi.api

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.renderapi.test.RenderPlayerTest
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level

abstract class RenderApiPlayer(val autoRotate: Boolean = true) : RenderApi<RenderPlayerEvent.Post>() {

    protected var entity: Player? = null

    override fun getMultiBufferSource(event: RenderPlayerEvent.Post): MultiBufferSource.BufferSource {
        return buffer //TODO ???
    }

    override fun setUpRender(e: RenderPlayerEvent.Post) {
        event = e
        entity = e.entity
        poseStack = e.poseStack

        poseStack {
            translate(0.0, entity!!.nameTagOffsetY.toDouble(), 0.0)
            if (autoRotate) {
                poseStack!!.mulPose(Minecraft.getInstance().gameRenderer.mainCamera.rotation())
            }
            scaleForWorldRendering()

            render(e)
        }
    }

    protected fun autoRotate(x: Double, z: Double) {
        val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event!!.partialTick)
        val rotY = Mth.atan2(x - eye.x, z - eye.z)
        event!!.poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
    }

    protected fun autoRotateX(x: Double) {
        val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event!!.partialTick)
        val rotY = Mth.atan2(x - eye.x, 1.0)
        event!!.poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        val buffer
            get() = Minecraft.getInstance().renderBuffers().bufferSource() //TODO maybe custom buffer?
        val toRender = mutableListOf<RenderApiPlayer>()

        init {
            toRender.add(RenderPlayerTest)
            WarlordsPlusPlus.LOGGER.log(
                Level.INFO,
                "RenderApiPlayer registered ${toRender.size} renderers: ${toRender.joinToString { it::class.simpleName!! }}"
            )
        }

        @SubscribeEvent
        fun onWorldRender(event: RenderPlayerEvent.Post) {
            toRender.forEach {
                it.onRenderEvent(event)
            }
        }

    }


}