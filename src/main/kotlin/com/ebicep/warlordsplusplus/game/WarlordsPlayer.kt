package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.util.SpecType
import com.ebicep.warlordsplusplus.util.Specialization
import com.ebicep.warlordsplusplus.util.Team
import com.ebicep.warlordsplusplus.util.WarlordClass

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
}