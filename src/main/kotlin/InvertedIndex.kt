import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

class InvertedIndex {
    var stopwords = Arrays.asList(
        "a", "able", "about",
        "across", "after", "all", "almost", "also", "am", "among", "an",
        "and", "any", "are", "as", "at", "be", "because", "been", "but",
        "by", "can", "cannot", "could", "dear", "did", "do", "does",
        "either", "else", "ever", "every", "for", "from", "get", "got",
        "had", "has", "have", "he", "her", "hers", "him", "his", "how",
        "however", "i", "if", "in", "into", "is", "it", "its", "just",
        "least", "let", "like", "likely", "may", "me", "might", "most",
        "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
        "on", "only", "or", "other", "our", "own", "rather", "said", "say",
        "says", "she", "should", "since", "so", "some", "than", "that",
        "the", "their", "them", "then", "there", "these", "they", "this",
        "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
        "what", "when", "where", "which", "while", "who", "whom", "why",
        "will", "with", "would", "yet", "you", "your"
    )
    var index: MutableMap<String, MutableList<Tuple>> = HashMap()
    var files: MutableList<String> = ArrayList()

    @Throws(IOException::class)
    fun indexFile(file: File) {
        var filePath = file.path
        var fileNumber = files.indexOf(file.path)
        if (fileNumber == -1) {
            files.add(file.path)
            fileNumber = files.size - 1
        }
        var pos = 0
        val reader = BufferedReader(FileReader(file))
        var line = reader.readLine()
        val splitter = Regex("""\W+""")
        while (line != null) {
            for (_word in line.split(splitter).toTypedArray()) {
                val word = _word.toLowerCase(Locale.getDefault())
                pos++
                if (stopwords.contains(word)) continue
                var idx = index[word]
                if (idx == null) {
                    idx = LinkedList()
                    index[word] = idx
                }
                idx.add(Tuple(filePath,fileNumber,pos))
            }
            line = reader
                .readLine()
        }
        println("indexed " + file.path + " " + pos + " words")
    }

    fun search(words: List<String>) {
        for (_word in words) {
            val answer: MutableSet<String> = HashSet()
            val word = _word.lowercase(Locale.getDefault())
            val idx: List<Tuple>? = index[word]
            if (idx != null) {
                for (t in idx) {
                    answer.add(files[t.fileNumber])
                }
            }
            print(word)
            for (f in answer) {
                print(" $f")
            }
            println("")
        }
    }
}
