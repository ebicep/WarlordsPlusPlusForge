package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.events.WarlordsPlayerEvents
import com.ebicep.warlordsplusplus.util.Specialization
import com.ebicep.warlordsplusplus.util.Team
import com.ebicep.warlordsplusplus.util.WarlordClass
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
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
    var prestiged: Boolean = false
    var left: Boolean = false

    //TODO
    var died: Int = 0
    var stoleKill: Int = 0
    var picks: Int = 0
    var returns: Int = 0
    var caps: Int = 0
    var isDead: Boolean = false
    var respawn: Int = -1

    var currentEnergy: Int = 0
    var maxEnergy: Int = 0
    var redCooldown: Int = 0
    var purpleCooldown: Int = 0
    var blueCooldown: Int = 0
    var orangeCooldown: Int = 0

    // [MAG][80][+]
    fun getInfoPrefix(): MutableComponent {
        return Component.literal("")
            .withStyle {
                it.withColor(ChatFormatting.DARK_GRAY)
            }
            .append(Component.literal("["))
            .append(Component.literal(warlordClass.shortName)
                .withStyle {
                    it.withColor(ChatFormatting.WHITE)
                })
            .append(Component.literal("]["))
            .append(Component.literal(level.toString().padStart(2, '0'))
                .withStyle {
                    it.withColor(if (prestiged) ChatFormatting.GOLD else ChatFormatting.WHITE)
                })
            .append(Component.literal("]["))
            .append(spec.iconComponent)
            .append(Component.literal("]"))
    }
}

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object OtherWarlordsPlayers {

    val playersMap: MutableMap<String, OtherWarlordsPlayer> = mutableMapOf()
    //val tempPlayersMap: MutableMap<String, WarlordsPlayer> = mutableMapOf()

    fun getOtherWarlordsPlayers(): Collection<OtherWarlordsPlayer> {
        return getOtherWarlordsPlayers(Minecraft.getInstance().player!!.connection.onlinePlayers)
    }

    fun getOtherWarlordsPlayers(playersInfo: MutableCollection<PlayerInfo>): Collection<OtherWarlordsPlayer> {
        playersInfo.filter {
            !playersMap.contains(it.profile.name)
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
            playersMap[it.name] = it
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

    @SubscribeEvent
    fun onKill(event: WarlordsPlayerEvents.KillEvent) {
        if (event.deathPlayer in playersMap) {
            val otherWarlordsPlayer = playersMap[event.deathPlayer]!!
            otherWarlordsPlayer.deaths++
            otherWarlordsPlayer.isDead = true
            otherWarlordsPlayer.respawn = event.respawn
        }
        if (event.player in playersMap) {
            playersMap[event.player]!!.kills++
        }
    }

    @SubscribeEvent
    fun onAbstractDamageHealEnergyEvent(event: WarlordsPlayerEvents.AbstractDamageHealEnergyEvent) {
        val player = event.player
        val otherWarlordsPlayer = if (player in playersMap) playersMap[player]!! else return
        when (event::class) {
            WarlordsPlayerEvents.DamageDoneEvent::class -> otherWarlordsPlayer.damageDone += event.amount

            WarlordsPlayerEvents.DamageTakenEvent::class -> otherWarlordsPlayer.damageReceived += event.amount

            WarlordsPlayerEvents.HealingGivenEvent::class -> otherWarlordsPlayer.healingDone += event.amount

            WarlordsPlayerEvents.HealingReceivedEvent::class -> otherWarlordsPlayer.healingReceived += event.amount
        }
    }
}