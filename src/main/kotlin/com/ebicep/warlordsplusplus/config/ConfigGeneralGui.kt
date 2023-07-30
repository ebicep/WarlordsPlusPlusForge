package com.ebicep.warlordsplusplus.config

import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.OptionsList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ConfigGeneralGui(private val lastScreen: Screen?) : Screen(Component.translatable("warlordsplusplus.config.general.title")) {

    companion object {
        val enabled: OptionInstance<Boolean> = OptionInstance.createBoolean(
            "warlordsplusplus.config.general.enabled",
            OptionInstance.cachedConstantTooltip(Component.translatable("warlordsplusplus.config.general.enabled.tooltip")),
            Config.enabled.get()
        ) { Config.delayedUpdates[Config.enabled] = { Config.enabled.set(it) } }

        //StringWidget
        private fun percentValueLabel(component: Component, value: Double): Component {
            return Component.translatable("options.percent_value", component, (value * 100.0).toInt())
        }

        private fun genericValueLabel(pText: Component, pValue: Int): Component {
            return Options.genericValueLabel(pText, Component.literal(pValue.toString()))
        }
    }

    private lateinit var list: OptionsList

    override fun init() {
        this.list = OptionsList(
            minecraft!!, width, height,
            32,
            height - 32,
            25
        )
        this.list.addBig(enabled)

        this.addWidget(this.list)
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) {
                minecraft!!.options.save()
                minecraft!!.setScreen(lastScreen)
            }.bounds(this.width / 2 - 100, this.height - 29, 200, 20).build()
        )
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pGuiGraphics)
        list.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        pGuiGraphics.drawCenteredString(font, title, width / 2, 20, 16777215)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    override fun onClose() {
        minecraft!!.setScreen(lastScreen)
    }

}