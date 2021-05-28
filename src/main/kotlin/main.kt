import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis


fun test( size:Int, index: InvertedIndex){
    val MAX_THREADS = 200
    var table = "1\t"
    for(s in 1000..size step 250){
        table += "${measureTimeMillis { indexCreating(1,s,index)}}\t"
    }
    table += "\n"

    for(i in 2..MAX_THREADS step 4 ){
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


fun main(args: Array<String>) {

    try {
        val idx = InvertedIndex()
        val size = File("src/main/resources/train").list().size +
                File("src/main/resources/test").list().size
       /* indexCreating(4,size,idx)*/
        test(size,idx)
       /* println("Введите слово для поиска: ")
        idx.search(Arrays.asList(*readLine()!!.split(",").toTypedArray()))*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}