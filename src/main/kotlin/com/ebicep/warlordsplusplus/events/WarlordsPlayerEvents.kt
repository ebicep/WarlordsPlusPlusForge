package com.ebicep.warlordsplusplus.events

import com.ebicep.warlordsplusplus.game.GameStateManager
import net.minecraftforge.eventbus.api.Event

object WarlordsPlayerEvents {

    abstract class AbstractDamageHealEnergyEvent : Event() {
        abstract val amount: Int
        abstract val player: String
        abstract val isCrit: Boolean
        abstract val isAbsorbed: Boolean
        abstract val ability: String
        val minute: Int = GameStateManager.minute
    }

    data class HealingGivenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class DamageDoneEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class HealingReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class DamageTakenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyReceivedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyGivenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyStolenEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val ability: String = "",
    ) : AbstractDamageHealEnergyEvent()

    data class EnergyLostEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean = false,
        override val isAbsorbed: Boolean = false,
        override val ability: String = "",
    ) : AbstractDamageHealEnergyEvent()

    data class DamageAbsorbedEvent(
        override val amount: Int,
        override val player: String,
        override val isCrit: Boolean,
        override val isAbsorbed: Boolean = true,
        override val ability: String,
    ) : AbstractDamageHealEnergyEvent()

    data class KillEvent(
        val player: String,
        val deathPlayer: String,
        val time: Int,
        val respawn: Int = GameStateManager.currentGameMode.getCurrentRespawn(),
        val sysTime: Long = System.currentTimeMillis()
    ) : Event()

    data class KillStealEvent(val otherPlayer: String) : Event()

    data class HitEvent(val otherPlayer: String, val time: Int) : Event()
}