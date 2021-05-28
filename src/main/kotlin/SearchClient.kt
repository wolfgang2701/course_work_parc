import java.io.*
import java.net.Socket

// необхідні змінні для роботи клієнту
private var client: Socket? = null
private var reader: BufferedReader? = null
private var `in`: BufferedReader? = null
private var out: BufferedWriter? = null

fun main(args: Array<String>) {
    try {
        try {
            // спроба підключення клієнта до сервера
            client = Socket("localhost", 7000)
            // створюю необхідні Input/Output Streams для передачі інформації між клієнтом та сервером
            reader = BufferedReader(InputStreamReader(System.`in`))
            `in` = BufferedReader(InputStreamReader(client!!.getInputStream()))
            out = BufferedWriter(OutputStreamWriter(client!!.getOutputStream()))

            println("Введите слово для поиска: ")
            var searchWords = reader!!.readLine()
            out!!.write(
                """
                        $searchWords
                        
                        """.trimIndent()
            )
            out!!.flush()

            println(`in`!!.readLine())

        } finally {
            // закриваю клієнт та стріми
            println("Client 1 was closed")
            client!!.close()
            `in`!!.close()
            out!!.close()
            reader!!.close()
        }
    } catch (e: IOException) {
        System.err.println(e)
    }
}
