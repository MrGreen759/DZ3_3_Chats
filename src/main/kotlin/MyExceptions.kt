class ChatNotFoundException (message: String) : RuntimeException(message)

class UserNotFoundException (message: String = "Пользователь не зарегистрирован в системе.") : RuntimeException(message)