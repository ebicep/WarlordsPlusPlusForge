package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.util.SpecType
import com.ebicep.warlordsplusplus.util.Specialization
import com.ebicep.warlordsplusplus.util.Team
import com.ebicep.warlordsplusplus.util.WarlordClass
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object WarlordsPlayer {

    var healingGivenCounter = 0
        private set
    var damageDoneCounter = 0
        private set
    var energyReceivedCounter = 0
        private set
    var healingReceivedCounter = 0
        private set
    var damageTakenCounter = 0
        private set
    var energyGivenCounter = 0
        private set
    var energyStolenCounter = 0
        private set
    var energyLostCounter = 0
        private set
    var killParticipation = 0
        private set

    //minute
    //kill,death,hit,dmg,heal,dmg taken,heal received
    var minuteStat = Array(1) { IntArray(7) }
        private set

    var spec: Specialization = Specialization.NONE
    var superSpec: SpecType = SpecType.NONE
    var warlord: WarlordClass = WarlordClass.NONE
    var team: Team = Team.NONE


    @SubscribeEvent
    fun onAbstractDamageHealEnergyEvent(event: WarlordsPlayerEvents.AbstractDamageHealEnergyEvent) {
        when (event::class) {
            WarlordsPlayerEvents.DamageDoneEvent::class -> {
                damageDoneCounter += event.amount
                minuteStat[0][3] += event.amount
            }

            WarlordsPlayerEvents.HealingGivenEvent::class -> {
                healingGivenCounter += event.amount
                minuteStat[0][4] += event.amount
            }

            WarlordsPlayerEvents.DamageTakenEvent::class -> {
                damageTakenCounter += event.amount
                minuteStat[0][5] += event.amount
            }

            WarlordsPlayerEvents.HealingReceivedEvent::class -> {
                healingReceivedCounter += event.amount
                minuteStat[0][6] += event.amount
            }

            WarlordsPlayerEvents.EnergyReceivedEvent::class -> {
                energyReceivedCounter += event.amount
            }

            WarlordsPlayerEvents.EnergyGivenEvent::class -> {
                energyGivenCounter += event.amount
            }

            WarlordsPlayerEvents.EnergyStolenEvent::class -> {
                energyStolenCounter += event.amount
            }

            WarlordsPlayerEvents.EnergyLostEvent::class -> {
                energyLostCounter += event.amount
            }
        }
    }
}