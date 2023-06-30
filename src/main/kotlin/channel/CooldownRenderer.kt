package com.ebicep.warlordsplusplus.channel

import com.ebicep.warlordsplusplus.game.OtherWarlordsPlayers
import com.ebicep.warlordsplusplus.renderapi.api.RenderApiPlayer
import com.ebicep.warlordsplusplus.util.Colors
import com.ebicep.warlordsplusplus.util.ImageRegistry
import net.minecraftforge.client.event.RenderPlayerEvent

object CooldownRenderer : RenderApiPlayer() {

    override fun render(event: RenderPlayerEvent.Post) {
        //TODO fix transparency / depth / scaling
        val player = OtherWarlordsPlayers.playersMap[entity!!.uuid] ?: return
        poseStack {
            translateX(-51.5)
            translateY(75.0)
            scale(.4)

            if (player.redCooldown == 0) {
                renderImage(75, 75, ImageRegistry.RED_ABILITY)
            } else {
                drawCooldown(player.redCooldown)
            }
            translateX(60)
            if (player.purpleCooldown == 0) {
                renderImage(75, 75, ImageRegistry.PURPLE_ABILITY)
            } else {
                drawCooldown(player.purpleCooldown)
            }
            translateX(60)
            if (player.blueCooldown == 0) {
                renderImage(75, 75, ImageRegistry.BLUE_ABILITY)
            } else {
                drawCooldown(player.blueCooldown)
            }
            translateX(60)
            if (player.orangeCooldown == 0) {
                renderImage(75, 75, ImageRegistry.ORANGE_ABILITY)
            } else {
                drawCooldown(player.orangeCooldown)
            }
        }
        poseStack {
            translateY(37)
            val width = 75f
            poseStack {
                translateY(.5)
                renderRectXCentered(width + 2, 6f, Colors.BLACK)
            }
            translateZ(-.1)
            renderRectXCentered(width, 5f, Colors.GRAY)
            translateX(-width / 2 - .5)
            translateZ(-.1)
            renderRect(((player.currentEnergy.toDouble() / player.maxEnergy) * width).toFloat(), 5f, Colors.GOLD)
        }
        poseStack {
            translateY(39.5)
            translateZ(-.1)
            scale(1.5)
            player.currentEnergy.toString().drawCentered(color = Colors.GREEN)
        }
    }

    fun drawCooldown(cooldown: Int) {
        poseStack {
            scale(.9)
            translateX(5)
            translateY(-7)
            renderImage(70, 70, ImageRegistry.COOLDOWN)
            translate(30, 45)
            scale(4.2)
            val str = if (cooldown < 10) " $cooldown" else "$cooldown"
            str.draw()
        }
    }

}