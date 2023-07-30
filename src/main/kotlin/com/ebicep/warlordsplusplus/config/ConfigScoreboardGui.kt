package com.ebicep.warlordsplusplus.config

import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.OptionsList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraftforge.common.ForgeConfigSpec

class ConfigScoreboardGui(private val lastScreen: Screen?) : Screen(Component.translatable("warlordsplusplus.config.scoreboard.title")) {

    companion object {
        val enabled: OptionInstance<Boolean> = createBooleanOption("warlordsplusplus.config.scoreboard.enabled", Config.scoreboardEnabled)
        val scaleCTFTDM: OptionInstance<Double> = OptionInstance(
            "warlordsplusplus.config.scoreboard.scaleCTFTDM",
            OptionInstance.cachedConstantTooltip(Component.translatable("warlordsplusplus.config.scoreboard.scaleCTFTDM.tooltip")),
            { component: Component, value: Double ->
                if (value == 0.0) {
                    CommonComponents.optionStatus(component, false)
                } else {
                    percentValueLabel(component, value)
                }
            },
            OptionInstance.UnitDouble.INSTANCE,
            Config.scaleCTFTDM.get(),
            { Config.delayedUpdates[Config.scaleCTFTDM] = { Config.scaleCTFTDM.set(it) } }
        )
        var scaleDOM: OptionInstance<Double> = OptionInstance(
            "warlordsplusplus.config.scoreboard.scaleDOM",
            OptionInstance.cachedConstantTooltip(Component.translatable("warlordsplusplus.config.scoreboard.scaleDOM.tooltip")),
            { component: Component, value: Double ->
                if (value == 0.0) {
                    CommonComponents.optionStatus(component, false)
                } else {
                    percentValueLabel(component, value)
                }
            },
            OptionInstance.UnitDouble.INSTANCE,
            Config.scaleDOM.get(),
            { Config.delayedUpdates[Config.scaleDOM] = { Config.scaleDOM.set(it) } }
        )
        var showTopHeader: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.showTopHeader.enabled", Config.showTopHeader)
        var showOutline: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.showOutline.enabled", Config.showOutline)
        var showDiedToYouStoleKill: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.showDiedToYouStoleKill.enabled", Config.showDiedToYouStoleKill)
        var showDoneAndReceived: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.showDoneAndReceived.enabled", Config.showDoneAndReceived)
        var splitScoreBoard: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.splitScoreBoard.enabled", Config.splitScoreBoard)

        private fun createBooleanOption(name: String, variable: ForgeConfigSpec.ConfigValue<Boolean>): OptionInstance<Boolean> {
            return OptionInstance.createBoolean(
                name,
                OptionInstance.cachedConstantTooltip(Component.translatable("$name.tooltip")),
                variable.get()
            ) { Config.delayedUpdates[variable] = { variable.set(it) } }
        }

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
        this.list.addSmall(
            arrayOf(
                scaleCTFTDM, scaleDOM,
                showTopHeader, showOutline,
                showDiedToYouStoleKill, showDoneAndReceived,
                splitScoreBoard
            )
        )

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