package codelens.cssSelector

import co.elpache.codelens.codeSearch.parser.parseQuery
import co.elpache.codelens.codeSearch.search.ContextNode
import co.elpache.codelens.tree.CodeTree
import co.elpache.codelens.tree.verticeOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class ExpressionEvaluationTest {

  @Test
  fun `evaluate literal expressions`() {
    val exprWithInt = parseQuery("class[1]").selectors.first().expr
    val exprWithString = parseQuery("class['hello']").selectors.first().expr


    assertThat(exprWithInt.evaluate(mockContext())).isEqualTo(1)
    assertThat(exprWithString.evaluate(mockContext())).isEqualTo("hello")
  }

  @Test
  fun `evaluate reference`() {
    val exprWithReference = parseQuery("class[name]").selectors.first().expr

    assertThat(exprWithReference.evaluate(mockContext("name" to "Ricardo"))).isEqualTo("Ricardo")
  }

  @Test
  fun `evaluate binnary op`() {
    val exprWithReference = parseQuery("class[name = 1]").selectors.first().expr

    assertThat(exprWithReference.evaluate(mockContext("name" to 1))).isEqualTo(true)
    assertThat(exprWithReference.evaluate(mockContext("name" to 2))).isEqualTo(false)
  }

  @Test
  fun `evaluate more complex operation`() {
    val exprWithReference = parseQuery("class[name = 'a' || name = 'b']").selectors.first().expr

    assertThat(exprWithReference.evaluate(mockContext("name" to "c"))).isEqualTo(false)
    assertThat(exprWithReference.evaluate(mockContext("name" to "b"))).isEqualTo(true)
  }

  @Test
  fun `Support grouping`() {
    val exprWithReference = parseQuery("class[(1 + 2)*4=12]").selectors.first().expr

    assertThat(exprWithReference.evaluate(mockContext())).isEqualTo(true)
  }

  @Test
  fun `evaluate Aliasing`() {
    val aliasExpression = parseQuery("class[1 + 2 as aSum]").selectors.first().expr
    val context = mockContext()

    aliasExpression.evaluate(context)

    assertThat(context.vertice["aSum"]).isEqualTo(3.0)
  }

  @Test
  fun `Supports decimals`() {
    val aliasExpression = parseQuery("class[1.5 + 2.5 as aSum]").selectors.first().expr
    val context = mockContext()

    aliasExpression.evaluate(context)

    assertThat(context.vertice["aSum"]).isEqualTo(4.0)
  }

  private fun mockContext(vararg pair: Pair<String, Any>): ContextNode {
    val tree = CodeTree()
    tree.addVertice(verticeOf("1").addAll(*pair))
    return ContextNode("1", tree)
  }

}

