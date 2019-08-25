package co.elpache.codelens

fun StringBuilder.trimStart() {
  while (isNotEmpty() && first() == ' ')
    delete(0, 1)
}

fun String.unwrap() = substring(1, length - 1)

fun String.underscoreToCamel() =
  toLowerCase().replace(Regex("(.)_([a-zA-Z])")) {
    it.groupValues[1] + it.groupValues[2].toUpperCase()
  }


//Abreviates long code text to a shorter version for a summary view
fun String.abreviate() =
  this.trim()
    .replace("\n", " ")
    .replace(Regex("\\s+"), " ")
    .take(100)

/**
 * Regex to find firt quoted string, for example:
 *  x = 'hello'; y = 'something else';
 * will find 'hello' (Including quotes)
 */
const val QUOTED_STRING = "^([\"'])(?:(?=(\\\\?))\\2.)*?\\1"

/**
 * Matches wildcard patterns like
 * com.google.**SomeClass.kt
 * com.*.repository.AnyClass.kt
 */
fun matches(text: String, wildCardPattern: String): Boolean {
  var pattern = wildCardPattern.toLowerCase()
    .replace(".", "\\.")
    .replace("**", "[late]")
    .replace("*", "[^.]+")
    .replace("[late]", ".*")

  return text.toLowerCase().contains(Regex("^$pattern$"))
}

fun relevantCodeLines(code: String) =
  code.split("\n")
    .map{ it.replace("{", "").replace("}", "").replace(Regex("\\s+"), "") }
    .filter { !it.trim().isBlank() }.size