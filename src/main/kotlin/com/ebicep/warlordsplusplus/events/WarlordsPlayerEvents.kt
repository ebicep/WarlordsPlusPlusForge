package com.ebicep.warlordsplusplus.events

import net.minecraftforge.eventbus.api.Event

object WarlordsPlayerEvents {

    abstract class AbstractDamageHealEvent : Event() {
        abstract val amount: Int
        abstract val player: String
        abstract val isCrit: Boolean
        abstract val isAbsorbed: Boolean
        abstract val minute: Int
    }

    data class HealingGivenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class DamageDoneEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class HealingReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class DamageTakenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class EnergyReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class EnergyGivenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class EnergyStolenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class EnergyLostEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEvent()

    data class DamageAbsorbedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean,
        override val minute: Int
    ) : AbstractDamageHealEvent()

}