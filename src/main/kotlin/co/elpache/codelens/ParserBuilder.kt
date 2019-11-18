package co.elpache.codelens

open class Node(val text: String = "")
typealias  Children = ArrayList<Node>
typealias Code = StringBuilder
typealias AddChildrenCall = (n: Node) -> Unit
typealias ChildrenParser = ParserBuilder.() -> Unit

abstract class ParserBuilder {
  abstract fun take(code: Code): Node
  abstract fun lookAhead(code: Code): Boolean

  var definition: ParserBuilder.(code: Code) -> Map<String, List<Node>> = { mapOf() }

  fun take(code: String) = take(Code(code))

  fun define(def: ParserBuilder.(code: Code) -> Map<String, List<Node>>): ParserBuilder {
    definition = def
    return this
  }

  fun oneOf(vararg items: ParserBuilder, code: Code): List<Node> {
    return listOf(items.first { it.lookAhead(code) }.take(code))
  }

  fun one(p: ParserBuilder, code: Code): List<Node> {
    return listOf(p.take(code))
  }

  fun zeroOrOne(p: ParserBuilder, code: Code): List<Node> {
    if (p.lookAhead(code))
      return listOf(p.take(code))
    return listOf()
  }

  fun atLeastOne(p: ParserBuilder, code: Code): List<Node> {
    val nodes = ArrayList<Node>()
    nodes.add(one(p, code).first())
    nodes.addAll(many(p, code))
    return nodes
  }

  fun many(p: ParserBuilder, code: Code): List<Node> {
    val nodes = ArrayList<Node>()
    while (p.lookAhead(code))
      nodes.add(p.take(code))

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

    val children = definition(this, code)

    return converter(DefaultNode(text, children.values.flatten()))
  }

  override fun lookAhead(code: Code): Boolean = lookAhead.find(code) != null
}

typealias NodeConverter = (node: DefaultNode) -> Node

fun defaultParser(exp: String, lookAhead: String = exp, converter: NodeConverter = { node -> node }) =
  { DefaultParser(Regex(exp), Regex(lookAhead), converter) }

fun rootParser(converter: NodeConverter = { node -> node }) =
  { DefaultParser(Regex("^"), Regex("^"), converter) }