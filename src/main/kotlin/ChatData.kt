data class User(
    val userId: Int,
    val userName: String
)

data class Chat(
    val userId: Int,                                        // пользователь, начавший чат
    val corrId: Int,                                        // пользователь, с которым начат чат
    val chatId: Int = 0,                                    // id чата
    var counterIn: Int = 0,                                 // счетчик входящих сообщений
    var counterOut: Int = 0,                                // счетчик исходящих сообщений
    var counter: Int = 0,                                   // счетчик сообщений
    //var messages: MutableList<String> = mutableListOf(),    // список сообщений
    var messages: MutableList<Triple<String, String, Boolean>> = mutableListOf(),
    var unread: Boolean = false                             // есть ли непрочитанные входящие сообщения
) {
    override fun toString(): String {
        val corrName = ChatService.getNameById(corrId)
        val ur = if (unread) "\u001B[31mесть\u001B[0m" else "нет"
        return "------ Чат #$chatId\n Собеседник: $corrName\n Сообщений: $counter (непрочитанные: $ur)\n"
    }
}