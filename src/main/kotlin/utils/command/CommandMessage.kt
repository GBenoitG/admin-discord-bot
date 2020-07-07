package utils.command

import net.dv8tion.jda.api.entities.Message
import utils.properties.PropertiesManager

open class CommandMessage(
        val commandName: Commands.CommandName,
        val content: CommandContent) {



    companion object {

        fun parseCommandFromMessage(message: Message): CommandMessage? {

            val trimedMessage = message.contentDisplay.trim()
            val splitMessage = trimedMessage.split(' ')
            val commandDisplay = splitMessage[0]
            val rawContent = trimedMessage.replace(commandDisplay, "").trim()

            commandDisplay.findAnyOf(
                    strings = Commands.CommandName.getPrefixedCommandList(),
                    startIndex = 0,
                    ignoreCase = true
            ) ?: return null

            val command = Commands.CommandName.values()
                    .find { it.value.contains(commandDisplay.removePrefix(PropertiesManager.properties.prefix)) } ?: return null

            val content = when (command) {
                Commands.CommandName.COMMAND_PERMISSION ->
                    PermissionContent(rawContent)
                Commands.CommandName.COMMAND_OP,
                Commands.CommandName.COMMAND_DEOP -> {
                    if (message.mentionedMembers.isEmpty()) return null
                    OperatorContent(message.mentionedMembers, rawContent)
                }
                else -> EmptyContent()
            }

            return CommandMessage(command, content)

        }

    }

}