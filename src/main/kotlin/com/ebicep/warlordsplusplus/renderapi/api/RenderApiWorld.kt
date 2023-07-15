package com.ebicep.warlordsplusplus.renderapi.api

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.renderapi.test.RenderWorldTest
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level


abstract class RenderApiWorld : RenderApi<RenderLevelStageEvent>() {

    override fun setUpRender(e: RenderLevelStageEvent) {
        event = e
        poseStack = e.poseStack
        poseStack {

            scaleForWorldRendering()
            RenderSystem.enableDepthTest() // so that text doesnt look weird (semi transparent)
            RenderSystem.enableBlend()

            render(e)

            RenderSystem.disableDepthTest()
            RenderSystem.disableBlend()
        }
    }

    override fun getMultiBufferSource(event: RenderLevelStageEvent): MultiBufferSource.BufferSource {
        return buffer //TODO ???
    }

    protected fun autoRotate(x: Double, z: Double) {
        val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event!!.partialTick)
        val rotY = Mth.atan2(x - eye.x, z - eye.z)
        event!!.poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
    }

    protected fun autoRotateX(x: Double) {
        val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event!!.partialTick)
        val rotY = Mth.atan2(x - eye.x, 0.0)
        event!!.poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
    }

    protected fun autoRotateZ(z: Double) {
        val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event!!.partialTick)
        val rotY = Mth.atan2(0.0 - eye.z, z - eye.z)
        event!!.poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {

        val buffer
            get() = Minecraft.getInstance().renderBuffers().bufferSource() //TODO maybe custom buffer?
        val toRender = mutableListOf<RenderApiWorld>()

        init {
            toRender.add(RenderWorldTest)
            WarlordsPlusPlus.LOGGER.log(
                Level.INFO,
                "RenderApiWorld registered ${toRender.size} renderers: ${toRender.joinToString { it::class.simpleName!! }}"
            )
        }

        @SubscribeEvent
        fun onWorldRender(event: RenderLevelStageEvent) {
            // check for stage to prevent weird rendering + better performance
            if (event.stage != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return
            }
            toRender.forEach {
                it.onRenderEvent(event)
            }
            buffer.endBatch()
        }

    }

}