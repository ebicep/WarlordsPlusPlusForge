package com.ebicep.warlordsplusplus.game

import com.ebicep.warlordsplusplus.MODID
import com.ebicep.warlordsplusplus.WarlordsPlusPlus
import com.ebicep.warlordsplusplus.events.WarlordsGameEvents
import net.minecraft.client.Minecraft
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
    fun onReset(event: WarlordsGameEvents.ResetEvent) {

    }

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
        try {
            val oldTime = time
            time = currentGameMode.getTime(sortedTeams)
            if (time == null) {
                return
            }
            if (oldTime?.first != time?.first) {
                FORGE_BUS.post(WarlordsGameEvents.MinuteEvent(time!!.first))
            }
            if (oldTime?.second != time?.second) {
                FORGE_BUS.post(WarlordsGameEvents.SecondEvent(time!!.second))
            }
        } catch (e: Exception) {
            WarlordsPlusPlus.LOGGER.error("Error getting time", e)
        }
    }

}