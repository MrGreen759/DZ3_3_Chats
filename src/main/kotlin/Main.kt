fun main() {

    val user = User(12, "Я")
    val sergey = User(18, "Сергей")
    val ivan = User(21, "Иван")
    val anna = User (30, "Анна")

    ChatService.userList = mutableListOf(user, sergey, ivan, anna)

    val chat1 = ChatService.letsChat(12, 18, "Привет, Серега.")
    ChatService.letsChat(12, 18, "Куда пропал?")
    ChatService.receiveReply(18, "Привет. Да че-то все некогда.")
    ChatService.letsChat(12, 18, "Давай соберемся?")
    ChatService.receiveReply(18, "Давай.")
    ChatService.receiveReply(18, "Когда?")

    ChatService.viewChat(chat1)

    val chat2 = ChatService.letsChat(12, 21, "Здравствуйте, Петр.")
    ChatService.letsChat(12, 21, "Подскажите, всё ещё продаёте ноутбук?")
    ChatService.receiveReply(21, "Добрый день. Да.")
    ChatService.letsChat(12, 21, "Какова стоимость?")
    ChatService.receiveReply(21, "30 000. Возможен торг.")
    ChatService.readUnread(chat2)

    ChatService.viewChat(chat2)

    // редактирование сообщения
    // удаление сообщения
    ChatService.editMessage(chat2, 1, "Здравствуйте, Иван")
    ChatService.deleteMessage(chat2, 4)

    // обращение к несуществующему сообщению
    ChatService.deleteMessage(chat2, 24)

    ChatService.viewChat(chat2)

    val chat3 = ChatService.letsChat(12, 30, "Аня, привет")
    ChatService.receiveReply(30, "Привет")
    ChatService.letsChat(12, 30, "Данные по проекту пришли, плиз")
    ChatService.receiveReply(30, "Да я в отпуске. Выйду - пришлю")
    ChatService.letsChat(12, 30, "Нет, надо срочно. Руководство требует. Жду.")
    ChatService.receiveReply(30, "Блин... Ладно.")
    ChatService.letsChat(12, 30, "Сорри")
    ChatService.receiveReply(30, "Да ладно, ноутбук со мной же.")

    ChatService.viewChat(chat3)

    // получение заданного количества сообщений
    ChatService.viewChat(chat3, 3)
    ChatService.viewChat(chat3, 3, 3)

    // получение списка чатов пользователя
    println("\n" + ChatService.getChats(12))

    // получение количества непрочитанных чатов
    println("\nУ вас есть непрочитанные сообщения в ${ChatService.getUnreadChatsCount(12)} чатах.")

    // удаление чата
    ChatService.deleteChat(2)
    println("\n" + ChatService.getChats(12))

    // выбрасываем исключение при обращении к несуществующему чату
    ChatService.viewChat(chat2)

}