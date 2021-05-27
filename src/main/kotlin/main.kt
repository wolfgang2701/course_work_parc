import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentHashMap


/*internal class ThreadIndex(index: ConcurrentHashMap<String, MutableList<Tuple>>, startIndex: Int, endIndex: Int) :
    Thread() {
    private val fileNamesList: List<String> = ArrayList()
    private val index = ConcurrentHashMap<String, MutableList<Tuple>>()
    private val startIndex: Int
    private val endIndex: Int
    private val splitter = "\\W+"
    private val fileNames: MutableList<String> = ArrayList()
    override fun run() {
        for (i in startIndex..endIndex) {
            try {
                indexFile(fileNamesList[i])
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun indexFile(fileName: String) {

        fileNames.add(fileName)
        val allLines = Files.readAllLines(Paths.get(fileName))
        for (line in allLines) {
            var posIndex = 0
            for (word in line.toLowerCase().split(splitter).toTypedArray()) {
                val locations = index.computeIfAbsent(
                    word
                ) { k: String? -> ArrayList() }
                locations.add(Tuple(fileName, posIndex++))
            }
        }
        println("$fileName has been indexed")
    }


}*/




fun main(args: Array<String>) {
    try {
        val idx = InvertedIndex()
       /* for (i in 1 until args.size) {
            idx.indexFile(File("src/main/resources/train/neg/0_3.txt"))
        }*/
        idx.indexFile(File("src/main/resources/0_3.txt"))
        idx.search(Arrays.asList(*"who".split(",").toTypedArray()))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}