package com.ebicep.warlordsplusplus.events

import net.minecraftforge.eventbus.api.Event

object WarlordsPlayerEvents {

    abstract class AbstractDamageHealEnergyEvent : Event() {
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
    ) : AbstractDamageHealEnergyEvent()

    data class DamageDoneEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class HealingReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class DamageTakenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyGivenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyStolenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyLostEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class DamageAbsorbedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean,
        override val minute: Int
    ) : AbstractDamageHealEnergyEvent()

    data class KillEvent(
        val player: String,
        val deathPlayer: String,
        val time: Int,
        val respawn: Int,
        val sysTime: Long = System.currentTimeMillis()
    ) : Event()

    data class KillStealEvent(val otherPlayer: String) : Event()

}