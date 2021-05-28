import java.io.File

class ThreadIndex(private val endIndex: Int,
                  private val startIndex: Int,
                  private val index: InvertedIndex): Thread() {

    private val pathString = arrayListOf<String>("src/main/resources/train","src/main/resources/test")

    override fun run() {

        val trainFiles = File(pathString[0]).list()
        val testFiles = File(pathString[1]).list()

        val delta =  1500
        for (i in startIndex until endIndex){
            if (i < delta){
                index.indexFile(pathString[0]+"/"+trainFiles[i])
            } else {
                index.indexFile(pathString[1]+"/"+testFiles[i - delta])//исправить i!!!
            }

        }
    }
}