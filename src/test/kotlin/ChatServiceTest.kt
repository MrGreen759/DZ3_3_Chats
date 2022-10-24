import org.junit.Before
import org.junit.Test
import kotlin.test.*

class ChatServiceTest {

    @Before
    fun beforeTest() {
        ChatService.prepareForTests()
    }

    @Test
    fun newChatAndAddMessage() {
        val result = ChatService.letsChat(12, 18, "Привет, Серега.")
        assertEquals(1, result)
    }

    @Test
    fun chatExists() {
        ChatService.letsChat(12, 18, "Привет, Серега.")
        val result = ChatService.checkChatIsExists(12,18)
        assertEquals(1, result)
    }

    @Test
    fun chatNotExists() {
        ChatService.letsChat(12, 18, "Привет, Серега.")
        val result = ChatService.checkChatIsExists(12,21)
        assertEquals(0, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun shouldThrow() {
        ChatService.editMessage(4,4,"New message")
    }

    @Test
    fun getNChats() {
        ChatService.letsChat(12, 18, "Привет, Серега.")
        ChatService.letsChat(12, 18, "Как дела?")
        ChatService.letsChat(12, 30, "Привет, Аня.")
        val result = ChatService.getNChats(12,2)
        assertNotNull(result)
    }

    @Test
    fun isUserFoundThrow() {
        ChatService.isUserFound(12)
    }

    @Test
    fun getMessages() {
        ChatService.letsChat(12, 18, "Привет, Серега.")
        ChatService.letsChat(12, 18, "Как дела?")
        val result = ChatService.getMessages(1,2,1)
        assertIs<String>(result)
    }

}