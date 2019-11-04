package co.elpache.codelens.codetree

abstract class LangEntity(
  val astType: String,
  type: String,
  name: String? = null,
  val startOffset: Int,
  val endOffset: Int,
  val codeFile: CodeFile? = null
) : CodeEntity(name, type) {


  open val code: String
    get() =
      if (codeFile!!.isNotEmpty()
        && startOffset <= endOffset
        && startOffset < codeFile.code.length
        && endOffset <= codeFile.code.length
      )
        codeFile.code.substring(startOffset, endOffset)
      else {
        //System.err.println("Problem with node $data")
        ""
      }

  init {
    data.addAll(
      "astType" to astType,
      "name" to name,
      "type" to type,
      "startOffset" to startOffset,
      "endOffset" to endOffset
    )
  }

}