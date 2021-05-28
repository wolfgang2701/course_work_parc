import java.io.*
import java.net.ServerSocket

// необхідні змінні для роботи серверу
private var server: ServerSocket? = null
private var in1: BufferedReader? = null
private var out1: BufferedWriter? = null
private var in2: BufferedReader? = null
private var out2: BufferedWriter? = null

fun main(args: Array<String>) {
    try {
        try {
            // прив'язую серверний сокет до порта
            server = ServerSocket(7000)
            println("Server is started!")
            // прослуховую під'єднання першого клієнта до сокета
            val client1 = server!!.accept()
            println("Client 1 connected!")
            // прослуховую під'єднання другого клієнта до сокета
            val client2 = server!!.accept()
            println("Client 2 connected!")
            try {
                // створюю необхідні змінні для вводу/виводу інформації на клієнти через InputStream та OutputStream
                in1 = BufferedReader(InputStreamReader(client1.getInputStream()))
                out1 = BufferedWriter(OutputStreamWriter(client1.getOutputStream()))
                in2 = BufferedReader(InputStreamReader(client2.getInputStream()))
                out2 = BufferedWriter(OutputStreamWriter(client2.getOutputStream()))
                out1!!.write("Welcome to the Chat! \n")
                out1!!.flush()
                out2!!.write("Welcome to the Chat! \n")
                out2!!.flush()
                out1!!.write("Please, write your name and age:) \n")
                out1!!.flush()
                out2!!.write("Please, write your name and age:) \n")
                out2!!.flush()

                // запитую у першого клієнта ім'я та вік
                val cliWord1 = in1!!.readLine()
                println("Client 1: $cliWord1")

                // запитую у другого клієнта ім'я та вік
                val cliWord2 = in2!!.readLine()
                println("Client 2: $cliWord2")

                // вивожу другому клієнту ім'я та вік першого, якщо отриманий рядок не пустий
                if (cliWord1 != null) {
                    out2!!.write("Your collocutor on client 1 is $cliWord1 \n")
                } else {
                    out2!!.write("Your collocutor on client 1 wrote nothing \n")
                }
                out2!!.flush()

                // вивожу першому клієнту ім'я та вік другого, якщо отриманий рядок не пустий
                if (cliWord2 != null) {
                    out1!!.write("Your collocutor on client 2 is $cliWord2 \n")
                } else {
                    out1!!.write("Your collocutor on client 2 wrote nothing \n")
                }
                out1!!.flush()
            } finally {
                // закриваю клієнт, Input/OutputStreams
                client1.close()
                client2.close()
                in1!!.close()
                out1!!.close()
                in2!!.close()
                out2!!.close()
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