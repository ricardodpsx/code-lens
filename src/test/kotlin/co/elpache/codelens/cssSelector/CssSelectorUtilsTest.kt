package codelens.cssSelector

import co.elpache.codelens.codetree.NodeData
import co.elpache.codelens.codetree.nodeDataOf
import co.elpachecode.codelens.cssSelector.AttributeSelector
import co.elpachecode.codelens.cssSelector.matchesAttribute
import org.assertj.core.api.Assertions.*
import org.junit.Test

class CssSelectorUtilsTest {

  @Test
  fun `Select attributes by name only`() {
    val myObj = nodeDataOf("firstName" to "hello")
    assertThat(matchesAttribute(AttributeSelector("firstName"), myObj)).isTrue()
    assertThat(matchesAttribute(AttributeSelector("lastName"), myObj)).isFalse()
  }

  @Test
  fun `Select attributes by query`() {
    val myObj = nodeDataOf(
      "firstName" to "hello world"
    )

    assertThat(evaluate(myObj, "firstName", "=", "hello world")).isTrue()
    assertThat(evaluate(myObj, "firstName", "=", "hello worldss")).isFalse()

    assertThat(evaluate(myObj, "firstName", "*=", "llo wor")).isTrue()
    assertThat(evaluate(myObj, "firstName", "*=", "abc")).isFalse()

    assertThat(evaluate(myObj, "firstName", "^=", "hello")).isTrue()
    assertThat(evaluate(myObj, "firstName", "^=", "ello")).isFalse()

    assertThat(evaluate(myObj, "firstName", "$=", "world")).isTrue()
    assertThat(evaluate(myObj, "firstName", "$=", "worl")).isFalse()
  }


  private fun evaluate(obj: NodeData, name: String, op: String? = null, search: String? = null) =
    matchesAttribute(AttributeSelector(name, op, search), obj)
}
