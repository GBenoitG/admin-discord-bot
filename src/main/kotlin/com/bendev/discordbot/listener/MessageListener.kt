package com.bendev.discordbot.listener

import com.bendev.discordbot.utils.command.CommandMessage
import com.bendev.discordbot.utils.command.Commands
import com.bendev.discordbot.utils.command.OperatorContent
import com.bendev.discordbot.utils.command.PermissionContent
import com.bendev.discordbot.utils.properties.PropertiesManager
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)

        if (event.author.isBot || event.isFromType(ChannelType.PRIVATE)) return

        val jda = event.jda
        val commandMessage = CommandMessage.parseCommandFromMessage(event.message) ?: return

        if (!PropertiesManager.isUserAllowed(event.guild, event.author)) {
            event.message.addReaction("❌").queue()
            return
        }

        when (commandMessage.commandName) {
            Commands.CommandName.COMMAND_PERMISSION -> {
                commandMessage.content as PermissionContent
                commandMessage.content.reactions.forEach {
                    if (it.first.isEmpty()) throw Exception("Emote part is empty")

                    if (it.first.matches(Regex("""(:[^:\s]*(?:::[^:\s]*)*:)"""))) {
                        val emote = jda.getEmotesByName(it.first.replace(":", ""), true).first()
                        event.message.addReaction(emote).queue()
                    } else {
                        event.message.addReaction(it.first).queue()
                    }

                }

            }

            Commands.CommandName.COMMAND_HELP -> {
                event.author.openPrivateChannel().queue {
                    it.sendMessage("Help content comes soon").queue()
                }
            }

            Commands.CommandName.COMMAND_OP,
            Commands.CommandName.COMMAND_DEOP -> {
                commandMessage.content as OperatorContent
                val newProperties = PropertiesManager.properties
                commandMessage.content.mentionedMembers.map {
                    it.id
                }.forEach {
                    if (!newProperties.allowedUsersId.contains(it)) {
                        if (commandMessage.commandName == Commands.CommandName.COMMAND_OP) {
                            newProperties.allowedUsersId.add(it)
                        }
                    } else {
                        if (commandMessage.commandName == Commands.CommandName.COMMAND_DEOP) {
                            newProperties.allowedUsersId.remove(it)
                        }
                    }
                }

                PropertiesManager.updateProperties(newProperties)
            }

        }

    }

}