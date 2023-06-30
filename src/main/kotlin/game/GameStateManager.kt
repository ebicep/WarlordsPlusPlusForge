package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.events.WarlordsEvents
import com.ebicep.warlordsplusplus.util.ScoreboardUtils
import net.minecraft.client.Minecraft
import net.minecraft.world.scores.PlayerTeam
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object GameStateManager {

    val scoreboardTeamCheck = "team_" // for checking valid sidebar teams
    var inWarlords = false
    var inWarlords2 = false
    val inPvP: Boolean
        get() = currentGameMode != GameModes.NONE && GameModes.isPvP(currentGameMode)
    val inPvE: Boolean
        get() = currentGameMode != GameModes.NONE && !inPvP
    var currentGameMode: GameModes? = null


    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent.System) {
        val message = event.message
        val unformattedText = ScoreboardUtils.getUnformattedText(message)
        if (
            unformattedText == "The gates will fall in 1 second!" ||
            (inWarlords2 && unformattedText == "The game starts in 1 second!")
        ) {
            FORGE_BUS.post(WarlordsEvents.ResetEvent())
            WarlordsPlusPlus.LOGGER.log(Level.DEBUG, "Posted ResetEvent")
        }

    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        val message = event.message
        if (message == "TEST") {
            OtherWarlordsPlayers.getOtherWarlordsPlayers()
        }
    }

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START || Minecraft.getInstance().isPaused || Minecraft.getInstance().player == null) {
            return
        }
        val scoreboard = Minecraft.getInstance().player?.scoreboard ?: return
        val sidebarObjective = scoreboard.getDisplayObjective(1) ?: return
        inWarlords = ScoreboardUtils.getUnformattedText(sidebarObjective.displayName).contains("WARLORDS", ignoreCase = true)
        inWarlords2 = ScoreboardUtils.getUnformattedText(sidebarObjective.displayName).contains("WARLORDS 2.0", ignoreCase = true)
        if (!inWarlords) {
            currentGameMode = GameModes.NONE
            return
        }
        val sortedTeams = scoreboard.playerTeams
            .filter { it.name.contains(scoreboardTeamCheck) }
            .toList()
            .sortedBy {
                val name = it.name
                name.substring(name.indexOf("_") + 1).toInt()
            }
        currentGameMode = GameModes.values().first { it.isCurrent(sortedTeams) }
    }

    enum class GameModes {
        CTF {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "RED Flag", 8)
            }
        },
        TDM {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "BLU", 10)
            }
        },
        DOM {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "/2000", 12)
            }
        },

        //INTERCEPTION,
        WAVE_DEFENSE {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "Wave", 11)
            }
        },
        ONSLAUGHT {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAtAnywhere(sidebar, "Soul Energy")
            }
        },
        NONE {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return true // true in case no other gamemode is found
            }
        }

        ;

        abstract fun isCurrent(sidebar: List<PlayerTeam>): Boolean

        companion object {
            fun isPvP(gameMode: GameModes?): Boolean {
                if (gameMode == null) {
                    return false
                }
                when (gameMode) {
                    CTF, TDM, DOM -> true
                    else -> false
                }
                return false
            }
        }
    }

}