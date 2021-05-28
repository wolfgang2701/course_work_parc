import java.io.*
import java.net.ServerSocket
import java.util.*
import kotlin.system.measureTimeMillis


fun test( size:Int, index: InvertedIndex){
    val MAX_THREADS = 600
    var table = "1\t"
    for(s in 1000..size step 250){
        table += "${measureTimeMillis { indexCreating(1,s,index)}}\t"
    }
    table += "\n"

    for(i in 2..MAX_THREADS step 10 ){
        table += "$i\t"
        for(s in 1000..size step 250){
            table += "${measureTimeMillis { indexCreating(i,s,index) }}\t"
        }
        table += "\n"
    }
   println(table)
}

fun indexCreating(
    threadsNumber: Int,
    size:Int,
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

// необхідні змінні для роботи серверу
private var server: ServerSocket? = null
private var in1: BufferedReader? = null
private var out1: BufferedWriter? = null


fun main(args: Array<String>) {
    val idx = InvertedIndex()

    try {
        val size = File("src/main/resources/train").list().size +
                File("src/main/resources/test").list().size
        indexCreating(20,size,idx)
//        test(size,idx)
       /*
        idx.search(Arrays.asList(*readLine()!!.split(",").toTypedArray()))*/
        println(idx.search(Arrays.asList(*"yellow".split(",").toTypedArray())))
    } catch (e: Exception) {
        e.printStackTrace()
    }

    try {
        try {
            // прив'язую серверний сокет до порта
            server = ServerSocket(7000)
            println("Server is started!")
            // прослуховую під'єднання першого клієнта до сокета
            val client1 = server!!.accept()
            println("Client connected!")
            try {
                // створюю необхідні змінні для вводу/виводу інформації на клієнти через InputStream та OutputStream
                in1 = BufferedReader(InputStreamReader(client1.getInputStream()))
                out1 = BufferedWriter(OutputStreamWriter(client1.getOutputStream()))
                /*out1!!.write("Welcome to the Chat! \n")
                out1!!.flush()*/

                val searchWords = in1!!.readLine()
                println("Client 1: $searchWords")

                val searchRes = idx.search(Arrays.asList(*searchWords.split(",").toTypedArray()))

                out1!!.write(searchRes)
                out1!!.flush()


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