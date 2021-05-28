import java.io.*
import java.net.Socket

// необхідні змінні для роботи клієнту
private var client: Socket? = null
private var reader: BufferedReader? = null
private var `in`: BufferedReader? = null
private var out: BufferedWriter? = null

private const val PORT = 7000

fun search() {
    println("Введите слово для поиска: ")
    var searchWords = reader!!.readLine()
    out!!.write(
        """
                        $searchWords
                        
                        """.trimIndent()
    )
    out!!.flush()

    println(`in`!!.readLine())
}

fun close() {
    println("Client 1 was closed")
    client!!.close()
    `in`!!.close()
    out!!.close()
    reader!!.close()
}

fun main() {
    try {
        try {
            // спроба підключення клієнта до сервера
            client = Socket("localhost", PORT)
            // створюю необхідні Input/Output Streams для передачі інформації між клієнтом та сервером
            reader = BufferedReader(InputStreamReader(System.`in`))
            `in` = BufferedReader(InputStreamReader(client!!.getInputStream()))
            out = BufferedWriter(OutputStreamWriter(client!!.getOutputStream()))

            search()
        } finally {
            // закриваю клієнт та стріми
            close()
        }
    } catch (e: IOException) {
        System.err.println(e)
    }
}
