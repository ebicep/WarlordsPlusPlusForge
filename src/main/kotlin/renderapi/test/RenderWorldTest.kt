package com.ebicep.warlordsplusplus.renderapi.test

import com.ebicep.warlordsplusplus.renderapi.api.RenderApiWorld
import com.ebicep.warlordsplusplus.util.Colors
import net.minecraftforge.client.event.RenderLevelStageEvent


object RenderWorldTest : RenderApiWorld() {

    override fun render(event: RenderLevelStageEvent) {
        poseStack {
            val x = -2547.0
            val y = 55.0
            val z = 740.0
            translateToPos(x, y, z)

            poseStack {
                autoRotate(x, z)
                scaleForWorldRendering()

                renderRectXCentered(200f, 100f, Colors.AQUA)
//                renderImage(207f, 203f, ImageRegistry.WEIRDCHAMP )
//                val text = "HELLO WORLD"
//                text.drawCentered()
            }
        }
    }
}

//poseStack {
//
//                // Translate to world origin
//                val offset: Vec3 = Minecraft.getInstance().gameRenderer.mainCamera.position
//                poseStack.translate(-offset.x, -offset.y, -offset.z)
//
//                // Disable depth test to make text visible behind objects
////                RenderSystem.disableDepthTest()
////                RenderSystem.depthMask(true)
//                RenderSystem.enableDepthTest()
//                RenderSystem.depthMask(false)
//
//                val x = -2547.0
//                val y = 55.0
//                val z = 740.0
//
//                val buffer = MultiBufferSource.immediate(Tesselator.getInstance().builder)
//
//                poseStack {
//
//                    // Translate to coordinate
//                    poseStack.translate(x, y, z)
//
//                    // Rotate to face camera
//                    val eye: Vec3 = Minecraft.getInstance().player!!.getEyePosition(event.partialTick)
//                    val rotY = Mth.atan2(x - eye.x, z - eye.z)
//                    poseStack.mulPose(Axis.YP.rotation(rotY.toFloat()))
//
//                    val SCALE = 1F/16F
//                    val FLIP = Axis.ZP.rotation(Mth.PI)
//
//                    // Rotate to upright
//                    //poseStack.mulPose(FLIP)
////                    poseStack.scale(SCALE, SCALE, SCALE)
//                    poseStack.scale(-0.025F, -0.025F, -0.025F)
//
//
//
////                    RenderSystem.setShader { GameRenderer.getPositionTexShader() }
////                    RenderSystem.setShaderTexture(0, ImageRegistry.WEIRDCHAMP.getResourceLocation()!!)
////                    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
////                    val pPose = poseStack.last().pose()
////                    bufferBuilder
////                        .vertex(pPose, 0f, 0f, 0f)
////                        .uv(0f, 0f)
////                        .endVertex()
////                    bufferBuilder
////                        .vertex(pPose, 0f, height, 0f)
////                        .uv(0f, 1f)
////                        .endVertex()
////                    bufferBuilder
////                        .vertex(pPose, width, height, 0f)
////                        .uv(1f, 1f)
////                        .endVertex()
////                    bufferBuilder
////                        .vertex(pPose, width, 0f, 0f)
////                        .uv(1f, 0f)
////                        .endVertex()
////                    tesselator.end()
//
//                    // Center on coordinate
//                    val text = "HELLO WORLD"
//                    val centerOffset = Minecraft.getInstance().font.width(text) / 2.0
//                    //poseStack.translate((-centerOffset).toDouble(), -Minecraft.getInstance().font.lineHeight.toDouble(), 0.0)
//                    poseStack.translate(-centerOffset, 0.0, 0.0)
//
//                    Minecraft.getInstance().font.drawInBatch(
//                        text,
//                        0f,
//                        0f,
//                        0xFFFFFF,
//                        false,
//                        poseStack.last().pose(),
//                        buffer,
//                        Font.DisplayMode.NORMAL,
//                        0,
//                        0xF000F0
//                    )
//
//
//
//                }
//
//                buffer.endBatch()
//            }