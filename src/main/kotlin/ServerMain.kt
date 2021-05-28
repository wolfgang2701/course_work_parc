import java.io.*
import java.net.ServerSocket
import java.util.*
import kotlin.system.measureTimeMillis


const val THREADS_NUMBER = 20
private const val PORT = 7000

fun test(size: Int, index: InvertedIndex) {
    val MAX_THREADS = 600
    var table = "1\t"
    for (s in 1000..size step 250) {
        table += "${measureTimeMillis { indexCreating(1, s, index) }}\t"
    }
    table += "\n"

    for (i in 2..MAX_THREADS step 10) {
        table += "$i\t"
        for (s in 1000..size step 250) {
            table += "${measureTimeMillis { indexCreating(i, s, index) }}\t"
        }
        table += "\n"
    }
    println(table)
}

fun indexCreating(
    threadsNumber: Int,
    size: Int,
    index: InvertedIndex
) {
    val threadArray: Array<ThreadIndex?> = arrayOfNulls(threadsNumber)

    for (i in 0 until threadsNumber) {
        threadArray[i] = ThreadIndex(
            if (i == threadsNumber - 1) size else size / threadsNumber * (i + 1),
            (size / threadsNumber) * i,
            index
        )
        threadArray[i]!!.start()
    }

    for (i in 0 until threadsNumber) {
        threadArray[i]!!.join()
    }
}

fun handleClient(in1: BufferedReader?, out1: BufferedWriter?, idx: InvertedIndex) {
    val searchWords = in1!!.readLine()
    println("Client 1: $searchWords")

    val searchRes = idx.search(Arrays.asList(*searchWords.split(",").toTypedArray()))

    out1!!.write(searchRes)
    out1.flush()
}

fun main() {
    val idx = InvertedIndex()

    try {
        val size = File("src/main/resources/train").list().size +
                File("src/main/resources/test").list().size
        indexCreating(THREADS_NUMBER, size, idx)
//        test(size,idx)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // необхідні змінні для роботи серверу
    var server: ServerSocket? = null
    var in1: BufferedReader? = null
    var out1: BufferedWriter? = null

    try {
        try {
            // прив'язую серверний сокет до порта
            server = ServerSocket(PORT)
            println("Server is started!")
            // прослуховую під'єднання першого клієнта до сокета
            val client1 = server.accept()
            println("Client connected!")

            try {
                // створюю необхідні змінні для вводу/виводу інформації на клієнти через InputStream та OutputStream
                in1 = BufferedReader(InputStreamReader(client1.getInputStream()))
                out1 = BufferedWriter(OutputStreamWriter(client1.getOutputStream()))

                handleClient(in1, out1, idx)
            } finally {
                // закриваю клієнт, Input/OutputStreams
                client1.close()
                in1!!.close()
                out1!!.close()
            }
        } finally {
            // закриваю сервер
            println("Server is closed!")
            server!!.close()
        }
    } catch (e: IOException) {
        System.err.println(e)
    }
}