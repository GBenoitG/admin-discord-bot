package utils.command

import net.dv8tion.jda.api.entities.Member

class OperatorContent(val mentionedMembers: List<Member>, rawContent: String) : CommandContent(rawContent)