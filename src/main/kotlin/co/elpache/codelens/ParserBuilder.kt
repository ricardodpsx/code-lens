package co.elpache.codelens

typealias Code = StringBuilder

typealias ParserDefinition<T> = ParserBuilder<T>.(text: String) -> T

abstract class ParserBuilder<T>(
  val definition: ParserDefinition<T>
) {
  fun parse(code: String) = takeSet(Code(code))

  abstract fun take(code: Code): T
  abstract fun lookAhead(code: Code): Boolean

  var code: Code = Code("")

  fun takeSet(code: Code): T {
    this.code = code
    return take(code)
  }

  fun <R> oneOf(vararg items: ParserBuilder<R>): R = items.first { it.lookAhead(code) }.takeSet(code)


  fun <R> one(p: ParserBuilder<R>): R = p.takeSet(code)


  fun <R> zeroOrOne(p: ParserBuilder<R>): R? {
    if (p.lookAhead(code))
      return p.takeSet(code)
    return null
  }

  fun <R> atLeastOne(p: ParserBuilder<R>): List<R> {
    val nodes = ArrayList<R>()
    nodes.add(one(p))
    nodes.addAll(many(p))
    return nodes
  }

  fun <R> many(p: ParserBuilder<R>): List<R> {
    val nodes = ArrayList<R>()
    while (p.lookAhead(code)) nodes.add(p.takeSet(code))
    return nodes
  }
}

fun takeRegex(input: StringBuilder, regex: Regex, expecting: String) =
  try {
    takeString(input, regex.find(input)!!.value)
  } catch (e: Exception) {
    error("Expecting $expecting found '$input'")
  }

fun takeString(input: StringBuilder, found: String): String {
  input.delete(0, found.length)
  input.trimStart()
  return found.trim()
}

open class DefaultParser<T>(
  val exp: Regex,
  val lookAhead: Regex = exp,
  definition: ParserDefinition<T>
) : ParserBuilder<T>(definition) {

  override fun take(code: Code): T {
    return definition(this, takeRegex(code, exp, "valid type selectors $exp"))
  }

  override fun lookAhead(code: Code): Boolean = lookAhead.find(code) != null
}

fun <T> defaultParser(exp: String, lookAhead: String = exp, definition: ParserDefinition<T>) =
  DefaultParser(Regex(exp), Regex(lookAhead), definition)

fun textParser(exp: String, lookAhead: String = exp, definition: ParserDefinition<String> = { text -> text }) =
  DefaultParser(Regex(exp), Regex(lookAhead), definition)

fun <T> rootParser(definition: ParserDefinition<T>) =
  DefaultParser(Regex("^"), Regex("^"), definition)