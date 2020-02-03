package codelens.cssSelector

import co.elpachecode.codelens.cssSelector.AliasExpression
import co.elpachecode.codelens.cssSelector.BinnaryExpression
import co.elpachecode.codelens.cssSelector.Expression
import co.elpachecode.codelens.cssSelector.LiteralExpression
import co.elpachecode.codelens.cssSelector.NameExpression
import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.Relation
import co.elpachecode.codelens.cssSelector.RelationType
import co.elpachecode.codelens.cssSelector.SelectorFunction
import co.elpachecode.codelens.cssSelector.parseQuery
import co.elpachecode.codelens.cssSelector.parseSetQuery
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class SelectorParserTest {

  @Test
  fun cssParserSimpleSelect() {

    val root = parseQuery("class annotationEntry")

    assertThat(root.selectors[0].name).isEqualTo("class")
    assertThat(root.selectors[0].relation).isEqualTo(Relation("children", RelationType.FOLLOW_RELATION))
    assertThat(root.selectors[1].name).isEqualTo("annotationEntry")
  }

  @Test
  fun cssSelectWithAttributes() {
    val attr = parseQuery("class annotationEntry[name]").selectors[1].expr as NameExpression
    assertThat(attr.value).isEqualTo("name")

    val attrWithFilter = parseQuery("class annotationEntry[name='hello']").selectors[1].expr as BinnaryExpression


    assertThat((attrWithFilter.left as NameExpression).value).isEqualTo("name")
    assertThat(attrWithFilter.op).isEqualTo("=")
    assertThat((attrWithFilter.right as LiteralExpression).value).isEqualTo("hello")
  }


  fun leftValue(exp: Expression) = ((exp as BinnaryExpression).left as NameExpression).value
  fun rightValue(exp: Expression) = ((exp as BinnaryExpression).right as LiteralExpression).value


  @Test
  fun `Support all operators`() {
    listOf("*=", "=", "^=", "$=", "<", ">", ">=", "<=", "!=").forEach { op ->
      println(op)
      val type = parseQuery("myClass[lines $op 6]").selectors[0].expr as BinnaryExpression
      assertThat(type.op).isEqualTo(op)
    }
  }

  @Test
  fun `Select by numeric attribute`() {
    val type = parseQuery("myClass[lines=6]").selectors[0].expr
    assertThat(rightValue(type)).isEqualTo(6)
  }

  @Test
  fun cssSelectWithExtendedAttributeOperations() {
    val attr = parseQuery("class annotationEntry[firstChildrenName$='hello']").selectors[1].expr as BinnaryExpression
    assertThat(leftValue(attr)).isEqualTo("firstChildrenName")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(rightValue(attr)).isEqualTo("hello")
  }

  @Test
  fun `Supports pseudo childs`() {
    val sel = parseQuery("class :methods").selectors[1]

    assertThat(sel.name).isEqualTo(":methods")
  }


  @Test
  fun cssSupportEscape() {
    val attr =
      parseQuery("class annotationEntry[firstChildrenName$='hello \\'world\\'']").selectors[1].expr as BinnaryExpression
    assertThat(leftValue(attr)).isEqualTo("firstChildrenName")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(rightValue(attr)).isEqualTo("hello \\'world\\'")
  }

  @Test
  fun supportDirectDescendantRelations() {
    val type = parseQuery("class>method").selectors
    assertThat(type[0].name).isEqualTo("class")
    assertThat(type[0].relation.type == RelationType.DIRECT_RELATION).isTrue()
    assertThat(type[1].name).isEqualTo("method")
  }

  @Test
  fun `Select by id`() {
    val type = parseQuery("#myClass").selectors[0]
    assertThat(type.attributeToMatch).isEqualTo("name")
    assertThat(type.name).isEqualTo("myClass")
  }

  @Test
  fun `Select by current node`() {
    val type = parseQuery("$").selectors[0]
    assertThat(type.name).isEqualTo("$")

    val type2 = parseQuery("$ fun").selectors
    assertThat(type2[0].name).isEqualTo("$")
  }


  @Test
  fun `Select by any node`() {
    val type = parseQuery("*[lines=1]").selectors[0]
    assertThat(type.name).isEqualTo("*")
    assertThat(leftValue(type.expr)).isEqualTo("lines")
  }


  @Test
  fun `Can parse aggregators`() {
    val selector = parseQuery("fun | count()")
    assertThat(selector.aggregator!!.name).isEqualTo("count")
  }

  @Test
  fun `Can parse aggregators with parameters`() {
    val selector = parseQuery("a b c | sum(a, b)")
    assertThat(selector.aggregator!!.name).isEqualTo("sum")
    assertThat(selector.aggregator!!.params).extracting("value").containsExactly("a", "b")
  }


  @Test
  fun `Can have Set queries to set new parameters into nodes`() {
    val query = parseSetQuery("SET {c d} numOfArguments = {a b | count()}")
    val setter = query.paramSetters.first()
    val nodesToSet = query.nodesToSet

    assertThat(nodesToSet.selectors).extracting("name").containsExactly("c", "d")
    assertThat(setter.query.selectors).extracting("name").containsExactly("a", "b")
    assertThat(setter.paramName).isEqualTo("numOfArguments")
  }

  @Test
  fun `Can have nested queries to select parents`() {
    val pseudoAttribute = parseQuery("fun[{try exception}]").selectors.first()

    assertThat((pseudoAttribute.expr as Query).selectors).extracting("name").containsExactly("try", "exception")
  }


  @Test
  fun `Can have functions`() {
    val f = parseQuery("element[fun(1)]").selectors.first().expr

    assertThat((f as SelectorFunction).name).isEqualTo("fun")
    assertThat((f.params[0] as LiteralExpression).value).isEqualTo(1)
  }

  @Test
  fun `Can have no arg functions`() {
    val f = parseQuery("element[fun()]").selectors.first().expr

    assertThat((f as SelectorFunction).name).isEqualTo("fun")
    assertThat(f.params).isEmpty()
  }


  @Test
  fun `Can have aliasing expressions`() {
    val f = parseQuery("element[fun() as myResult]").selectors.first().expr as AliasExpression

    assertThat(f.name).isEqualTo("myResult")
    assertThat((f.expr as SelectorFunction).name).isEqualTo("fun")
  }

  @Test
  fun `Can have relationship expressions`() {
    val f = parseQuery("a -friends> b").selectors.first()

    assertThat(f.relation.name).isEqualTo("friends")
    assertThat(f.relation.type).isEqualTo(RelationType.DIRECT_RELATION)
  }
}

