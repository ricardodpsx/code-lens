package codelens.cssSelector

import co.elpache.codelens.codeSearch.search.matchesAttribute
import co.elpache.codelens.tree.VData
import co.elpache.codelens.tree.vDataOf
import co.elpachecode.codelens.cssSelector.AttributeSelector
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CssSelectorUtilsTest {

  @Test
  fun `Select attributes by name only`() {
    val myObj = vDataOf("firstName" to "hello")
    assertThat(matchesAttribute(AttributeSelector("firstName"), myObj)).isTrue()
    assertThat(matchesAttribute(AttributeSelector("lastName"), myObj)).isFalse()
  }

  @Test
  fun `Select attributes by query`() {
    val myObj = vDataOf(
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

  @Test
  fun `Numeric operators`() {
    val myObj = vDataOf(
      "firstName" to "4"
    )

    assertThat(evaluate(myObj, "firstName", "<", "5")).isTrue()
    assertThat(evaluate(myObj, "firstName", ">", "5")).isFalse()
    assertThat(evaluate(myObj, "firstName", "<=", "4")).isTrue()
    assertThat(evaluate(myObj, "firstName", ">=", "5")).isFalse()
  }


  private fun evaluate(obj: VData, name: String, op: String? = null, search: String? = null) =
    matchesAttribute(AttributeSelector(name, op, search), obj)
}
