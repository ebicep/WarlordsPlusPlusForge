package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import com.ebicep.warlordsplusplus.util.ScoreboardUtils
import net.minecraft.client.Minecraft
import net.minecraft.world.scores.PlayerTeam
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

private const val scoreboardTeamCheck = "team_" // for checking valid sidebar teams

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object GameStateManager {

    var inWarlords = false
    var inWarlords2 = false
    val inPvP: Boolean
        get() = currentGameMode != GameModes.NONE && currentGameMode.isPvP()
    val inPvE: Boolean
        get() = currentGameMode != GameModes.NONE && !inPvP
    var currentGameMode: GameModes = GameModes.NONE
    val inGame: Boolean
        get() = currentGameMode != GameModes.NONE
    val notInGame: Boolean
        get() = !inGame
    private var time: Pair<Int, Int>? = null
    val minute: Int
        get() = time?.first ?: 0
    val second: Int
        get() = time?.second ?: 0

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent.System) {
        val message = event.message
        val unformattedText = message.string
        if (
            unformattedText == "The gates will fall in 1 second!" ||
            (inWarlords2 && unformattedText == "The game starts in 1 second!")
        ) {
            FORGE_BUS.post(WarlordsGameEvents.ResetEvent())
            WarlordsPlusPlus.LOGGER.log(Level.DEBUG, "Posted ResetEvent")
        }

    }

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START || Minecraft.getInstance().isPaused || Minecraft.getInstance().player == null) {
            return
        }
        val scoreboard = Minecraft.getInstance().player?.scoreboard ?: return
        val sidebarObjective = scoreboard.getDisplayObjective(1) ?: return
        val unformattedDisplayName = sidebarObjective.displayName.string
        inWarlords = unformattedDisplayName.contains("WARLORDS", ignoreCase = true)
        inWarlords2 = unformattedDisplayName.contains("WARLORDS 2.0", ignoreCase = true)
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
        time = currentGameMode.getTime(sortedTeams)
    }

    enum class GameModes {
        CTF {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "RED Flag", 8)
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return getTimePvP(sidebar, 9)
            }
        },
        TDM {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "BLU", 10)
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return getTimePvP(sidebar, 6)
            }
        },
        DOM {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAt(sidebar, "/2000", 12)
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return getTimePvP(sidebar, 9)
            }
        },

        //INTERCEPTION,
        WAVE_DEFENSE {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAtAnywhere(sidebar, "Wave")
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return getTimePvE(sidebar, 4, 6)
            }
        },
        ONSLAUGHT {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return ScoreboardUtils.containsAtAnywhere(sidebar, "Soul Energy")
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return getTimePvE(sidebar, 4, 6)
            }
        },
        NONE {
            override fun isCurrent(sidebar: List<PlayerTeam>): Boolean {
                return true // true in case no other gamemode is found
            }

            override fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>? {
                return null
            }
        },

        ;

        abstract fun isCurrent(sidebar: List<PlayerTeam>): Boolean

        abstract fun getTime(sidebar: List<PlayerTeam>): Pair<Int, Int>?

        fun isPvP(): Boolean {
            return when (this) {
                CTF, TDM, DOM -> true
                else -> false
            }
        }

        fun getTimePvP(sidebar: List<PlayerTeam>, index: Int): Pair<Int, Int>? {
            return ScoreboardUtils.getAt(sidebar, index)?.let {
                val timeString: String =
                    if (it.contains("Wins")) it.substring(13)
                    else it.substring(11)
                val colonPosition = timeString.indexOf(":")
                return Pair(
                    timeString.substring(0, colonPosition).toInt(), //.coerceAtMost(14),
                    timeString.substring(colonPosition + 1).toInt()
                )
            }
        }

        fun getTimePvE(sidebar: List<PlayerTeam>, index: Int, timeStringIndex: Int): Pair<Int, Int>? {
            return ScoreboardUtils.getAt(sidebar, index)?.let {
                val timeString: String = it.substring(timeStringIndex)
                val colonPosition = timeString.indexOf(":")
                return Pair(
                    timeString.substring(0, colonPosition).toInt(), //.coerceAtMost(14),
                    timeString.substring(colonPosition + 1).toInt()
                )
            }
        }
    }
}