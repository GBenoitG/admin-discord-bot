package listener

import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.command.CommandMessage
import utils.command.PermissionContent

class ReactionListener : ListenerAdapter() {

    override fun onGenericMessageReaction(event: GenericMessageReactionEvent) {
        super.onGenericMessageReaction(event)

        event.channel.retrieveMessageById(event.messageId).queue { message ->
            val commandMessage = CommandMessage.parseCommandFromMessage(message) ?: return@queue

            when (commandMessage.content) {
                is PermissionContent -> {
                    val channelName = commandMessage.content.reactions.find {
                        if (event.reactionEmote.isEmoji)
                            it.first == event.reactionEmote.emoji
                        else
                            it.first.replace(":", "") == event.reactionEmote.emote.name
                    }?.second

                    if (event is MessageReactionAddEvent && channelName.isNullOrEmpty()) {
                        event.reaction.removeReaction(event.user!!).queue()
                        return@queue
                    }

                    val memberId = event.userId
                    val role = event.guild.roles.find { it.name == channelName }!!
                    when (event) {
                        is MessageReactionAddEvent ->
                            event.guild.addRoleToMember(memberId, role).queue()
                        is MessageReactionRemoveEvent ->
                            event.guild.removeRoleFromMember(memberId, role).queue()
                        else -> return@queue
                    }

                }

            }

        }

    }

}