package com.ebicep.warlordsplusplus.renderapi

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.Tesselator
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.player.LocalPlayer

abstract class RenderHelper {

    companion object {

        val tesselator: Tesselator
            get() = Tesselator.getInstance()

        val bufferBuilder: BufferBuilder = tesselator.builder

        val mc: Minecraft
            get() = Minecraft.getInstance()

        val player: LocalPlayer?
            get() = mc.player

        val font: Font
            get() = mc.font
    }
}