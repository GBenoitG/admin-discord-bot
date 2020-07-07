package utils.command

class PermissionContent(rawContent: String) : CommandContent(rawContent) {

    val description: String
    val reactions: ArrayList<Pair<String, String>>

    init {
        val description = rawContent.split(":")[0]
        val regex = """^(-.+)""".toRegex(RegexOption.MULTILINE)
        val array = arrayListOf<Pair<String, String>>()
        regex.findAll(rawContent).forEach { line ->
            array.add(
                    line.value
                            .slice(IntRange(2, line.value.length-1))
                            .trim()
                            .let {
                                Pair(
                                        it.split(' ')[0],
                                        it.split(' ')[1].replace("#", "")
                                )
                            }
            )
        }

        if (array.isEmpty()) throw Exception("Empty channel list, you must add some")

        this.description = description
        this.reactions = array
    }

}
