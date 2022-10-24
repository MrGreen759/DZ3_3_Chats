object ChatService {
    var userList: MutableList<User> = mutableListOf()
    var commonChatList: MutableList<Chat> = mutableListOf()
    private var chatCounter: Int = 0

    // отправка сообщения в чат. Если сообщение в адрес корреспондента - первое, создается новый чат.
    fun letsChat(sender: Int, correspondent: Int, message: String): Int {
        var chatIndex: Int
        var checkId = checkChatIsExists(sender, correspondent)
        if (checkId == 0) {
            chatCounter++
            val newChat = Chat(userId = sender, corrId = correspondent, chatId = chatCounter)
            commonChatList.add(newChat)
            checkId = chatCounter
        }
        chatIndex = getIndexById(checkId)

        commonChatList[chatIndex].counter++
        commonChatList[chatIndex].counterOut++
        //commonChatList[chatIndex].messages[commonChatList[chatIndex].counter] = Pair("out", message)
        commonChatList[chatIndex].messages.add(Triple("out", message, false))
        readUnread(checkId)

        return checkId
    }

    // проверка, существует ли чат с корреспондентом
    fun checkChatIsExists(uid: Int, cid: Int): Int {
        var tempList: List<Chat> = commonChatList.filter { chat: Chat -> chat.userId == uid }
        for (c in tempList) {
            if (c.corrId == cid) return c.chatId
        }
        return 0
    }

    // получение позиции чата (индекса) в списке чатов по его ID
    fun getIndexById(cid: Int): Int {
        var ind = -1
        for (i in 0..commonChatList.size-1) {
            if (commonChatList[i].chatId == cid) {
                ind = i
                break
            }
        }
        if (ind == -1) throw ChatNotFoundException("Чат #$cid не найден.")
        else return ind
    }

    // получение сообщения
    // помечается, как непрочитанное
    // помечается прочитанным 1) при получении списка сообщений чата, 2) при ответе
    fun receiveReply(corrId: Int, message: String) {
        var chatIndex = 0
        for (i in 0 .. commonChatList.size) {
            if (commonChatList[i].corrId == corrId) {
                chatIndex = getIndexById(commonChatList[i].chatId)
                break
            }
        }
        commonChatList[chatIndex].counter++
        commonChatList[chatIndex].counterIn++
        commonChatList[chatIndex].messages.add(Triple("in", message, true))
        commonChatList[chatIndex].unread = true
    }

    // редактирование сообщения
    fun editMessage(cid: Int, mid: Int, newText: String) {
        val chatIndex = getIndexById(cid)
        if (mid in 1..commonChatList[chatIndex].messages.size) {
            commonChatList[chatIndex].messages.set(
                mid-1,
                Triple(commonChatList[chatIndex].messages[mid-1].first, newText, false))
        } else println("\u001B[31mСообщение #$mid не найдено в чате #$cid.\u001B[0m")
    }

    // удаление сообщения
    fun deleteMessage(cid: Int, mid: Int) {
        val chatIndex = getIndexById(cid)
        if (mid in 1..commonChatList[chatIndex].messages.size) {
            commonChatList[chatIndex].messages.removeAt(mid - 1)
            if (commonChatList[chatIndex].messages.isEmpty()) deleteChat(cid)
        } else println("\u001B[31mСообщение #$mid не найдено в чате #$cid.\u001B[0m")
    }

    // чтение всех непрочитанных сообщений в чате
    // вызывается 1) при просмотре всего чата, 2) при ответе собеседнику
    fun readUnread(cid: Int) {
        val chatIndex = getIndexById(cid)
        val mess = commonChatList[chatIndex].messages
        for (i in 0..mess.size-1) {
            if (mess[i].first == "in" && mess[i].third) mess[i] = Triple(mess[i].first, mess[i].second, false)
        }
        commonChatList[chatIndex].unread = false
    }

    // получение информации о чатах пользователя
    fun getChats(uid: Int): List<Chat> {
        if (!isUserFound(uid)) throw UserNotFoundException()
        val chatList = commonChatList.filter { chat: Chat -> chat.userId == uid }
        if (chatList.isEmpty()) println("\u001B[31mУ вас нет активных чатов.\u001B[0m")
        return chatList
    }

    // получение информации об "n" чатах пользователя (Sequences)
    fun getNChats(uid: Int, n: Int): List<Chat> {
        return commonChatList.asSequence()
            .filter { chat: Chat -> chat.userId == uid }
            .ifEmpty { throw UserNotFoundException() }
            .take(n)
            .toList()
    }

    // получение информации о непрочитанных чатах
    fun getUnreadChatsCount(uid: Int): Int {
        val chatList = commonChatList.filter { chat: Chat -> (chat.userId == uid && chat.unread)}
        return chatList.size
    }

    // получение списка сообщений из определенного чата
    fun getMessages(cid: Int, bm: Int = 1, qm: Int = 0): String {
        val mess = commonChatList[getIndexById(cid)].messages
        return mess.drop(bm-1)
            .take(if(qm==0) mess.size else qm)
            .map{it.first + ": " + it.second}
            .joinToString(separator = "\n")
    }

    // просмотр чата
    fun viewChat(cid: Int, bm: Int = 1, qm: Int = 0) {
        val ind = getIndexById(cid)
        val chat = commonChatList[ind]
        val mess = chat.messages
        val corrName = getNameById(chat.corrId)
        var unread = ""
        val em = if (qm == 0) mess.size-1 else bm - 1 + qm - 1
        println("\n--------- ChatID: $cid (${mess.size} сообщений) ---------")
        for (i in (bm-1)..em) {
            if (mess[i].third) unread = ", \u001B[31mнепрочитано\u001B[0m" else unread = ""
            if (mess[i].first == "out") println("Вы (#" + (i+1) + "): " + mess[i].second)
            else println("        $corrName (#" + (i+1) + "$unread): " + mess[i].second)
        }
        println("--------- ChatID: $cid be continued ---------")
    }

    // удаление чата
    fun deleteChat(cid: Int) {
        val chatIndex = getIndexById(cid)
        commonChatList.removeAt(chatIndex)
    }

    // получение имени пользователя по ID
    fun getNameById(uid: Int): String {
        for (user in userList) {
            if (user.userId == uid) return user.userName
        }
        //return "Неизвестный собеседник"
        println(userList)
        throw UserNotFoundException("Пользователь не зерегистрирован в системе.")
    }

    fun isUserFound(uid: Int): Boolean {
        for (user in userList) {
            if (user.userId == uid) return true
        }
        return false
    }

    // подготовка к тестам
    fun prepareForTests() {
        userList = mutableListOf()
        commonChatList = mutableListOf()
        chatCounter = 0

        val user = User(12, "Я")
        val sergey = User(18, "Сергей")
        val ivan = User(21, "Иван")
        val anna = User (30, "Анна")

        ChatService.userList = mutableListOf(user, sergey, ivan, anna)

    }

}