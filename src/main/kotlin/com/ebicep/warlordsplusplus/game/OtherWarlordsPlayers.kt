package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.util.Specialization
import com.ebicep.warlordsplusplus.util.Team
import com.ebicep.warlordsplusplus.util.WarlordClass
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.world.scores.PlayerTeam
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.*


open class OtherWarlordsPlayer(val name: String, val uuid: UUID) {

    var kills: Int = 0
    var deaths: Int = 0
    var damageDone: Int = 0
    var damageReceived: Int = 0
    var healingDone: Int = 0
    var healingReceived: Int = 0
    var warlordClass = WarlordClass.NONE
    var spec = Specialization.NONE
    var team = Team.NONE
    var level = 0
    var left: Boolean = false

    var currentEnergy: Int = 0
    var maxEnergy: Int = 0
    var redCooldown: Int = 0
    var purpleCooldown: Int = 0
    var blueCooldown: Int = 0
    var orangeCooldown: Int = 0
}

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object OtherWarlordsPlayers {

    val playersMap: MutableMap<UUID, OtherWarlordsPlayer> = mutableMapOf()
    //val tempPlayersMap: MutableMap<String, WarlordsPlayer> = mutableMapOf()

    fun getOtherWarlordsPlayers(): Collection<OtherWarlordsPlayer> {
        return getOtherWarlordsPlayers(Minecraft.getInstance().player!!.connection.onlinePlayers)
    }

    fun getOtherWarlordsPlayers(playersInfo: MutableCollection<PlayerInfo>): Collection<OtherWarlordsPlayer> {
        playersInfo.filter {
            !playersMap.contains(it.profile.id)
        }.filter { playerInfo ->
            val playerTeam: PlayerTeam? = playerInfo.team
            if (playerTeam == null) {
                false
            } else {
                WarlordClass.values().any {
                    playerTeam.playerPrefix.string.contains(it.shortName)
                }
            }
        }.map { playerInfo ->
            val playerTeam: PlayerTeam = playerInfo.team!!
            val otherWarlordsPlayer = OtherWarlordsPlayer(playerInfo.profile.name, playerInfo.profile.id)
            otherWarlordsPlayer.warlordClass = WarlordClass.values().first {
                playerTeam.playerPrefix.string.contains(it.shortName)
            }
            otherWarlordsPlayer.team = Team.values().first {
                playerTeam.color == it.color
            }

            return@map otherWarlordsPlayer
        }.forEach {
            playersMap[it.uuid] = it
        }

        //TODO reupdate player

        return playersMap.values
    }

    fun getPlayerByName(name: String): OtherWarlordsPlayer? {
        return playersMap.values.firstOrNull { it.name == name }
    }

    @SubscribeEvent
    fun onReset(event: WarlordsGameEvents.ResetEvent) {
        playersMap.clear()
        getOtherWarlordsPlayers()
    }
}