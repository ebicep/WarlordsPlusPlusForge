package com.ebicep.warlordsplusplus.channel

import com.ebicep.warlordsplusplus.game.GameStateManager
import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import com.ebicep.warlordsplusplus.renderapi.api.RenderApiPlayer
import com.ebicep.warlordsplusplus.util.Colors
import com.ebicep.warlordsplusplus.util.ImageRegistry
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderPlayerEvent

object CooldownRenderer : RenderApiPlayer() {

    override fun shouldRender(event: RenderPlayerEvent.Post): Boolean {
        return GameStateManager.inWarlords2 &&
                GameStateManager.inGame &&
                (event.entity != Minecraft.getInstance().player) &&
                (GameStateManager.inPvE || !OtherWarlordsPlayers.playersMap.containsKey(Minecraft.getInstance().player?.scoreboardName))
    }

    override fun render(event: RenderPlayerEvent.Post) {
        val player = OtherWarlordsPlayers.playersMap[entity!!.scoreboardName] ?: return
        poseStack {
            translateX(-36.5)
            translateY(75.0)
            scale(.4)

            drawAbility(player.redCooldown, ImageRegistry.RED_ABILITY)
            translateX(60)
            drawAbility(player.purpleCooldown, ImageRegistry.PURPLE_ABILITY)
            translateX(60)
            drawAbility(player.blueCooldown, ImageRegistry.BLUE_ABILITY)
            translateX(60)
            drawAbility(player.orangeCooldown, ImageRegistry.ORANGE_ABILITY)
        }
        poseStack {
            translateY(37)
            val width = 75f
            poseStack {
                translateY(.5)
                renderRectXCentered(width + 2, 6f, Colors.BLACK)
            }
            translateZ(.01)
            renderRectXCentered(width, 5f, Colors.GRAY)
            translateX(-width / 2 - .5)
            translateZ(.01)
            renderRect(((player.currentEnergy.toDouble() / player.maxEnergy) * width).toFloat(), 5f, Colors.GOLD)
        }
        poseStack {
            translateY(39.5)
            translateZ(.05)
            scale(1.5)
            player.currentEnergy.toString().drawCentered(color = Colors.GREEN)
        }
    }

    private fun drawAbility(cooldown: Int, image: ImageRegistry) {
        if (cooldown == 0) {
            renderImage(75, 75, image)
        } else {
            renderCooldown(cooldown)
        }
    }

    private fun renderCooldown(cooldown: Int) {
        poseStack {
            scale(.9)
            translateX(5)
            translateY(-7)
            renderImage(70, 70, ImageRegistry.COOLDOWN)
            translate(-3.0, 45.0, .1)
            scale(4.2)
            val str = if (cooldown < 10) " $cooldown" else "$cooldown"
            str.draw()
        }
    }

}