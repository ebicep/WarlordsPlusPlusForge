package com.ebicep.warlordsplusplus.config

import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraftforge.common.ForgeConfigSpec

object ConfigUtils {

    fun createBooleanOption(name: String, variable: ForgeConfigSpec.ConfigValue<Boolean>): OptionInstance<Boolean> {
        return OptionInstance.createBoolean(
            name,
            OptionInstance.cachedConstantTooltip(Component.translatable("$name.tooltip")),
            variable.get()
        ) { Config.delayedUpdates[variable] = { variable.set(it) } }
    }

    fun createDoubleOption(name: String, variable: ForgeConfigSpec.ConfigValue<Double>): OptionInstance<Double> {
        return OptionInstance(
            name,
            OptionInstance.cachedConstantTooltip(Component.translatable("$name.tooltip")),
            { component: Component, value: Double ->
                if (value == 0.0) {
                    CommonComponents.optionStatus(component, false)
                } else {
                    percentValueLabel(component, value)
                }
            },
            OptionInstance.UnitDouble.INSTANCE,
            variable.get()
        ) { Config.delayedUpdates[variable] = { variable.set(it) } }
    }

    //StringWidget
    fun percentValueLabel(component: Component, value: Double): Component {
        return Component.translatable("options.percent_value", component, (value * 100.0).toInt())
    }

    fun genericValueLabel(pText: Component, pValue: Int): Component {
        return Options.genericValueLabel(pText, Component.literal(pValue.toString()))
    }

}