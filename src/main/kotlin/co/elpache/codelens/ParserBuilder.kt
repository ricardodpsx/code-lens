package co.elpache.codelens

open class Node(val text: String = "")
typealias  Children = ArrayList<Node>
typealias Code = StringBuilder
typealias AddChildrenCall = (n: Node) -> Unit
typealias ChildrenParser = ParserBuilder.() -> Unit

abstract class ParserBuilder {
  abstract fun take(code: Code): Node
  abstract fun lookAhead(code: Code): Boolean

  fun take(code: String) = take(Code(code))

  private val takeChildrenCallBacks = ArrayList<(Code, AddChildrenCall) -> Unit>()

  fun takeChildren(code: Code): List<Node> {
    return takeChildrenCallBacks.map {
      val children = Children()
      it(code) { n -> children.add(n) }
      children
    }.flatten()
  }

  fun oneOf(vararg items: ParserBuilder): ParserBuilder {

    takeChildrenCallBacks.add { code, child ->
      child(items.first { it.lookAhead(code) }.take(code))
    }

    return this
  }

  fun one(p: ParserBuilder, childrenParsers: ChildrenParser = {}): ParserBuilder {
    childrenParsers(p)
    takeChildrenCallBacks.add { code, child ->
      child(p.take(code))
    }
    return this
  }

  fun zeroOrOne(p: ParserBuilder, childrenParsers: ChildrenParser = {}): ParserBuilder {
    childrenParsers(p)
    takeChildrenCallBacks.add { code, child ->
      if (p.lookAhead(code))
        child(p.take(code))
    }
    return this
  }

  fun atLeastOne(p: ParserBuilder, childrenParsers: ChildrenParser = {}): ParserBuilder {
    childrenParsers(p)
    one(p)
    many(p)
    return this;
  }

  fun many(p: ParserBuilder, childrenParsers: ChildrenParser = {}): ParserBuilder {
    childrenParsers(p)
    takeChildrenCallBacks.add { code, child ->
      while (p.lookAhead(code))
        child(p.take(code))
    }
    return this
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

data class DefaultNode(var _text: String, val children: List<Node>) : Node(_text) {
  operator fun get(i: Int) = children.getOrNull(i)
}

open class DefaultParser(
  val exp: Regex,
  val lookAhead: Regex = exp,
  val converter: (node: DefaultNode) -> Node = { node -> node }
) : ParserBuilder() {

  override fun take(code: Code): Node {
    val text = takeRegex(code, exp, "valid type selectors $exp")
    val children = takeChildren(code)
    return converter(DefaultNode(text, children))
  }

  override fun lookAhead(code: Code): Boolean = lookAhead.find(code) != null
}

typealias NodeConverter = (node: DefaultNode) -> Node

fun defaultParser(exp: String, lookAhead: String = exp, converter: NodeConverter = { node -> node }) =
  { DefaultParser(Regex(exp), Regex(lookAhead), converter) }

fun rootParser(converter: NodeConverter = { node -> node }) =
  { DefaultParser(Regex("^"), Regex("^"), converter) }