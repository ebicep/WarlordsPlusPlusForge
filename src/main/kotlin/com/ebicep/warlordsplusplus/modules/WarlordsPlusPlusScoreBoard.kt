package com.ebicep.warlordsplusplus.modules

import com.ebicep.warlordsplusplus.game.*
import com.ebicep.warlordsplusplus.renderapi.api.RenderApiGuiOverride
import com.ebicep.warlordsplusplus.util.Colors
import com.ebicep.warlordsplusplus.util.Team
import net.minecraft.ChatFormatting
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.world.scores.Objective
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay

object WarlordsPlusPlusScoreBoard : RenderApiGuiOverride(VanillaGuiOverlay.PLAYER_LIST) {

    override fun shouldRender(event: RenderGuiOverlayEvent.Pre): Boolean {
        val scoreObjective: Objective? = mc.level!!.scoreboard.getDisplayObjective(0)
        val handler: ClientPacketListener = mc.player!!.connection

        return super.shouldRender(event) && GameStateManager.inGame &&
                (mc.options.keyPlayerList.isDown && (!mc.isLocalServer || handler.onlinePlayers.size > 1 || scoreObjective != null))
    }

    override fun render(event: RenderGuiOverlayEvent.Pre) {
        val thePlayer = player ?: return

        val players = OtherWarlordsPlayers.getOtherWarlordsPlayers(thePlayer.connection.onlinePlayers)

//        if (hideNewScoreboardPvE && GameStateManager.isPvE) {
//            return
//        }

        val teamBlue = players.filter { it.team == Team.BLUE }.sortedByDescending { it.level }
        val teamRed = players.filter { it.team == Team.RED }.sortedByDescending { it.level }


        val mostDeathsRed = if (teamRed.isEmpty()) 0 else teamRed.map { it.deaths }.sorted().reversed()[0]
        val mostDeathsBlue = if (teamBlue.isEmpty()) 0 else teamBlue.map { it.deaths }.sorted().reversed()[0]
        val mostKillsRed = if (teamRed.isEmpty()) 0 else teamRed.map { it.kills }.sorted().reversed()[0]
        val mostKillsBlue = if (teamBlue.isEmpty()) 0 else teamBlue.map { it.kills }.sorted().reversed()[0]

        var w = 443

        if (!showDoneAndReceived) {
            showDiedToYouStoleKill = false
        }

        if (!showDiedToYouStoleKill) {
            w -= 133
        } else if (!showTopHeader) {
            w -= 100
        }
        if (!showDoneAndReceived) {
            w -= 105
        }

        val yStart = 25
        var xStart = xCenter - (w / 2)



        if (GameStateManager.currentGameMode == GameModes.CTF || GameStateManager.currentGameMode == GameModes.TDM) {
            poseStack!!.scale(setScaleCTFTDM.toFloat(), setScaleCTFTDM.toFloat(), 1f)
            xStart =
                (xCenter + 50 - (setScaleCTFTDM.toDouble() * 100).toInt() / 2 - ((w * (setScaleCTFTDM.toDouble() * 100).toInt() / 100 / 2)))
        } else if (GameStateManager.currentGameMode == GameModes.DOM) {
            poseStack!!.scale(setScaleDOM.toFloat(), setScaleDOM.toFloat(), 1f)
            xStart = (xCenter + 50 - (setScaleDOM.toDouble() * 100).toInt() / 2 - ((w * (setScaleDOM.toDouble() * 100).toInt() / 100 / 2)))
        }

        xStart += moveScoreboard
        if (splitScoreBoard) {
            xStart -= 70
            if (showDoneAndReceived) {
                xStart -= 75
            }
        }

        var xLevel = 2.0
        var xName = 53.0
        var xKills = 100.0
        var xDeaths = 30.0
        var xDone = 40.0
        var xReceived = 50.0
        var xKilled = 60.0

        if (!showTopHeader) {
            xDeaths = 25.0
            xDone = 30.0
        }

        if (showTopHeader) {
            translate(xStart, yStart)
            renderRect(w, 13, Colors.DEF)
            poseStack {
                translateY(-3)
                translateX(xLevel + xName)
                "Name".draw()
                if (!showDoneAndReceived && !showDiedToYouStoleKill) {
                    translateX(xKills)
                    "K".draw()
                    translateX(xDeaths)
                    "D".draw()
                } else {
                    translateX(xKills)
                    "Kills".draw()
                    translateX(xDeaths)
                    "Deaths".draw()
                }
                if (showDoneAndReceived) {
                    translateX(xDone)
                    "Given".draw()
                    if (showTopHeader) {
                        if (showDiedToYouStoleKill)
                            translateX(xReceived)
                        else
                            translateX(xReceived - 12.5)
                        "Received".draw()
                    }
                }
                if (showDiedToYouStoleKill) {
                    translateX(xKilled)
                    "DiedToYou/StoleKill".draw()
                }
            }
            if (splitScoreBoard) {
                poseStack {
                    translateX(w + 5)
                    renderRect(w, 13, Colors.DEF)
                    translateY(-3)
                    translateX(xLevel + xName)
                    "Name".draw()
                    if (!showDoneAndReceived && !showDiedToYouStoleKill) {
                        translateX(xKills)
                        "K".draw()
                        translateX(xDeaths)
                        "D".draw()
                    } else {
                        translateX(xKills)
                        "Kills".draw()
                        translateX(xDeaths)
                        "Deaths".draw()
                    }
                    if (showDoneAndReceived) {
                        translateX(xDone)
                        "Given".draw()
                        if (showTopHeader) {
                            if (showDiedToYouStoleKill)
                                translateX(xReceived)
                            else
                                translateX(xReceived - 12.5)
                            "Received".draw()
                        }
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "DiedToYou/StoleKill".draw()
                    }
                }
            }
        } else {
            translate(xStart, yStart - 15)
        }

        fun renderLine(index: Int, p: OtherWarlordsPlayer) {
            if (showOutline) {
                translateY(-2) {
                    renderRect(w.toDouble(), 1.25, Colors.DEF)
                    renderRect(1.25, 11.0, Colors.DEF)
                }
                translate(w - 1.25, -2.0) {
                    renderRect(1.25, 11.0, Colors.DEF)
                }
                translateY(8.75) {
                    renderRect(w.toDouble(), 1.25, Colors.DEF)
                }
            } else {
                if (index % 2 == 1) {
                    translateY(-1.2) {
                        renderRect(w.toDouble(), 10.75, Colors.DEF, alpha = 40)
                    }
                }
            }

            fun hasMostKills(): Boolean {
                return if (p.team == Team.BLUE)
                    p.kills == mostKillsBlue
                else
                    p.kills == mostKillsRed
            }

            fun hasMostDeaths(): Boolean {
                return if (p.team == Team.BLUE)
                    p.deaths == mostDeathsBlue
                else
                    p.deaths == mostDeathsRed
            }

            fun drawFlag(): String {
//                if (p.hasFlag) {
//                    return if (p.team == Team.BLUE)
//                        "${ChatFormatting.RED}\u2690 "
//                    else
//                        "${ChatFormatting.BLUE}\u2690 "
//                }
                return ""
            }

            fun isPrestige(): String {
                return if (false)//p.prestiged)
                    ChatFormatting.GOLD.toString()
                else ""
            }

            fun level(level: Int): String {
                return if (level < 10)
                    "0${level}"
                else level.toString()
            }

            poseStack {
                translate(xLevel, .5)
                val isThePlayer = p.uuid == thePlayer.uuid
                "${ChatFormatting.GOLD}${p.warlordClass.shortName}${ChatFormatting.RESET} ${isPrestige()}${level(p.level)} ${if (isThePlayer) WarlordsPlayer.spec.icon else p.spec.icon}".draw()
                translateX(xName)
                "${drawFlag()}${
                    if (p.isDead) "${ChatFormatting.GRAY}${if (!GameStateManager.inWarlords2) "${p.respawn} " else ""}" else p.team.color.toString()
                }${
                    if (p.left) "${ChatFormatting.GRAY}${ChatFormatting.STRIKETHROUGH}" else ""
                }${
                    if (isThePlayer) ChatFormatting.GREEN else ""
                }${p.name}".draw()
                translateX(xKills)
                "${if (hasMostKills()) ChatFormatting.GOLD else ChatFormatting.RESET}${p.kills}".draw()
                translateX(xDeaths)
                "${if (hasMostDeaths()) ChatFormatting.DARK_RED else ChatFormatting.RESET}${p.deaths}".draw()
                if (WarlordsPlayer.team == p.team) {
                    if (showDoneAndReceived) {
                        translateX(xDone)
                        "${ChatFormatting.GREEN}${p.healingReceived}".draw()
                        translateX(xReceived)
                        "${ChatFormatting.DARK_GREEN}${p.healingDone}".draw()
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "${ChatFormatting.RESET}${p.stoleKill}".draw()
                    }
                } else {
                    if (showDoneAndReceived) {
                        translateX(xDone)
                        "${ChatFormatting.RED}${p.damageReceived}".draw()
                        translateX(xReceived)
                        "${ChatFormatting.DARK_RED}${p.damageDone}".draw()
                    }
                    if (showDiedToYouStoleKill) {
                        translateX(xKilled)
                        "${ChatFormatting.RESET}${p.died}".draw()
                    }
                }
            }

            translateY(-10.75)
        }

        if (splitScoreBoard) {
            translateY(-14)
            poseStack {
                renderRect(w.toDouble(), 10.75 * teamBlue.size + 1, Colors.DEF, 100)
                translateY(-2)
                teamBlue.forEachIndexed(::renderLine)
            }
            translateX(w + 5)
            poseStack {
                renderRect(w.toDouble(), 10.75 * teamRed.size + 1, Colors.DEF, 100)
                translateY(-2)
                teamRed.forEachIndexed(::renderLine)
            }
        } else {
            translateY(-14)
            renderRect(w.toDouble(), 10.75 * teamBlue.size + 1, Colors.DEF, 100)
            translateY(-2)
            teamBlue.forEachIndexed(::renderLine)

            translateY(-1)
            renderRect(w.toDouble(), 10.75 * teamRed.size + 1, Colors.DEF, 100)
            translateY(-2)
            teamRed.forEachIndexed(::renderLine)
        }
    }


    var showNewScoreboard = true
    var hideNewScoreboardPvE = true
    var setScaleDOM = ".8"
    var setScaleCTFTDM = ".8"
    var moveScoreboard = 0
    var showTopHeader = false
    var showOutline = true
    var showDiedToYouStoleKill = false
    var showDoneAndReceived = true
    var splitScoreBoard = true
}