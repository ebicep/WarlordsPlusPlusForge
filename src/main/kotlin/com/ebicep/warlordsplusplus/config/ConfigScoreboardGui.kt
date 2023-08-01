package com.ebicep.warlordsplusplus.config

import com.ebicep.warlordsplusplus.config.ConfigUtils.createBooleanOption
import net.minecraft.client.OptionInstance
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.OptionsList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ConfigScoreboardGui(private val lastScreen: Screen?) : Screen(Component.translatable("warlordsplusplus.config.scoreboard.title")) {

    companion object {
        val enabled: OptionInstance<Boolean> =
            createBooleanOption("warlordsplusplus.config.scoreboard.enabled", Config.scoreboardEnabled)
        val scaleCTFTDM: OptionInstance<Double> =
            ConfigUtils.createDoubleOption("warlordsplusplus.config.scoreboard.scaleCTFTDM", Config.scaleCTFTDM)
        var scaleDOM: OptionInstance<Double> =
            ConfigUtils.createDoubleOption("warlordsplusplus.config.scoreboard.scaleDOM", Config.scaleDOM)
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