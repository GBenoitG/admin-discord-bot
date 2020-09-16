package com.bendev.discordbot.listener

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PrivateMessageListener : ListenerAdapter() {

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        super.onPrivateMessageReceived(event)

        if (event.author.isBot) return

        println(event.jda.selfUser.mutualGuilds)
        val discords = event.jda.selfUser.mutualGuilds.joinToString(", ") { it.id }
        event.channel.sendMessage(discords).queue()

    }

}