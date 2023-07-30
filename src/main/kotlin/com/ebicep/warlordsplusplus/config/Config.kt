package com.ebicep.warlordsplusplus.config

import net.minecraftforge.common.ForgeConfigSpec
import kotlin.concurrent.fixedRateTimer

object Config {

    val GENERAL_SPEC: ForgeConfigSpec
    lateinit var enabled: ForgeConfigSpec.BooleanValue

    lateinit var scoreboardEnabled: ForgeConfigSpec.BooleanValue
    lateinit var scaleCTFTDM: ForgeConfigSpec.ConfigValue<Double>
    lateinit var scaleDOM: ForgeConfigSpec.ConfigValue<Double>
    lateinit var showTopHeader: ForgeConfigSpec.BooleanValue
    lateinit var showOutline: ForgeConfigSpec.BooleanValue
    lateinit var showDiedToYouStoleKill: ForgeConfigSpec.BooleanValue
    lateinit var showDoneAndReceived: ForgeConfigSpec.BooleanValue
    lateinit var splitScoreBoard: ForgeConfigSpec.BooleanValue

    // values that need to be updated, runs every 10 seconds to prevent spam saving
    val delayedUpdates: HashMap<Any, () -> Unit> = HashMap()

    init {
        val builder = ForgeConfigSpec.Builder()
        setupConfig(builder)
        GENERAL_SPEC = builder.build()
        //ChatPlus.LOGGER.info("Initialized config.")
        fixedRateTimer(period = 10 * 1000) {
            delayedUpdates.forEach { it.value() }
            delayedUpdates.clear()
        }
    }

    private fun setupConfig(builder: ForgeConfigSpec.Builder) {
        builder.push("General")
        enabled = builder.define("enabled", true)
        builder.pop()

        builder.push("Scoreboard")
        scoreboardEnabled = builder.define("scoreboardEnabled", true)
        scaleCTFTDM = builder.define("scaleCTFTDM", 0.8)
        scaleDOM = builder.define("scaleDOM", 0.8)
        showTopHeader = builder.define("showTopHeader", true)
        showOutline = builder.define("showOutline", false)
        showDiedToYouStoleKill = builder.define("showDiedToYouStoleKill", false)
        showDoneAndReceived = builder.define("showDoneAndReceived", true)
        splitScoreBoard = builder.define("splitScoreBoard", false)

    }

}