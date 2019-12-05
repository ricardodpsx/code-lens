package codelens.cssSelector

import co.elpachecode.codelens.cssSelector.Query
import co.elpachecode.codelens.cssSelector.RelationTypes
import co.elpachecode.codelens.cssSelector.parseCssSelector
import co.elpachecode.codelens.cssSelector.setQueryParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CssSelectorParserTest {

  @Test
  fun cssParserSimpleSelect() {
    val root = parseCssSelector("class annotationEntry")

    assertThat(root.selectors[0].name).isEqualTo("class")
    assertThat(root.selectors[1].name).isEqualTo("annotationEntry")
  }

  @Test
  fun cssSelectWithAttributes() {
    val attr = parseCssSelector("class annotationEntry[name]").selectors[1].attributes[0]
    assertThat(attr.name).isEqualTo("name")

    val attrWithFilter = parseCssSelector("class annotationEntry[name='hello']").selectors[1].attributes[0]

    assertThat(attrWithFilter.name).isEqualTo("name")
    assertThat(attrWithFilter.op).isEqualTo("=")
    assertThat(attrWithFilter.value).isEqualTo("hello")
  }


  @Test
  fun `Support all operators`() {
    listOf("*=", "=", "^=", "$=", "<", ">", ">=", "<=", "!=").forEach { op ->
      val type = parseCssSelector("myClass[lines $op 6]").selectors[0]
      assertThat(type.attributes[0].op).isEqualTo(op)
    }
  }

  @Test
  fun `Select by numeric attribute`() {
    val type = parseCssSelector("myClass[lines=6]").selectors[0]
    assertThat(type.attributes[0].value).isEqualTo("6")
  }

  @Test
  fun cssSelectWithExtendedAttributeOperations() {
    val attr = parseCssSelector("class annotationEntry[firstChildren-name$='hello']").selectors[1].attributes[0]
    assertThat(attr.name).isEqualTo("firstChildren-name")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(attr.value).isEqualTo("hello")
  }

  @Test
  fun cssSelectWithMultipleAttributeSelectors() {
    val attrs =
      parseCssSelector("annotationEntry[firstChildren-name$='ricardo'][last-name='pacheco']").selectors[0].attributes
    assertThat(attrs[0].value).isEqualTo("ricardo")
    assertThat(attrs[1].value).isEqualTo("pacheco")
  }

  @Test
  fun cssSupportEscape() {
    val attr =
      parseCssSelector("class annotationEntry[firstChildren-name$='hello \\'world\\'']").selectors[1].attributes[0]
    assertThat(attr.name).isEqualTo("firstChildren-name")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(attr.value).isEqualTo("hello \\'world\\'")
  }

  @Test
  fun supportDirectDescendantRelations() {
    val type = parseCssSelector("class>method").selectors
    assertThat(type[0].name).isEqualTo("class")
    assertThat(type[0].relationType.type == RelationTypes.DIRECT_DESCENDANT).isTrue()
    assertThat(type[1].name).isEqualTo("method")
  }

  @Test
  fun `Select by id`() {
    val type = parseCssSelector("#myClass").selectors[0]
    assertThat(type.attributeToMatch).isEqualTo("name")
    assertThat(type.name).isEqualTo("myClass")
  }

  @Test
  fun `Select by current node`() {
    val type = parseCssSelector("$").selectors[0]
    assertThat(type.name).isEqualTo("$")
  }


  @Test
  fun `Select by any node`() {
    val type = parseCssSelector("*[lines=1]").selectors[0]
    assertThat(type.name).isEqualTo("*")
    assertThat(type.attributes[0].name).isEqualTo("lines")
  }


  @Test
  fun `Can parse aggregators`() {
    val selector = parseCssSelector("a b c | count")
    assertThat(selector.func!!.op).isEqualTo("count")
  }

  @Test
  fun `Can parse aggregators with parameters`() {
    val selector = parseCssSelector("a b c | sum(param)")
    assertThat(selector.func!!.op).isEqualTo("sum")
    assertThat(selector.func!!.params).containsExactly("param")
  }

  @Test
  fun `Can parse aggregators with muoltiple parameters`() {
    val selector = parseCssSelector("a b c | sum(param , 'another param', 123)")
    assertThat(selector.func!!.op).isEqualTo("sum")
    assertThat(selector.func!!.params).containsExactly("param", "another param", "123")
  }


  @Test
  fun `Can have Set queries to set new parameters into nodes`() {
    val query = setQueryParser.parse("SET (c d) numOfArguments = (a b | count)")
    val setter = query.paramSetters.first()
    val nodesToSet = query.nodesToSet

    assertThat(nodesToSet.selectors).extracting("name").containsExactly("c", "d")
    assertThat((setter.setFunction as Query).selectors).extracting("name").containsExactly("a", "b")
    assertThat(setter.paramName).isEqualTo("numOfArguments")
  }

//  @Test
//  fun `Can have Set queries to set values`() {
//    funcRegistry["constant"] = { _, _ -> 1 }
//    val query = setQueryParser.parse("SET (c d) numOfArguments = constant(1) ")
//    val setter = query.paramSetters.first()
//    val nodesToSet = query.nodesToSet
//  }

//  @Test
//  fun `Can have nested queries to select parents`() {
//    val pseudoAttribute = parseCssSelector("fun[has(try exception)]").selectors.first()
//
//    assertThat(pseudoAttribute.op).isEqualTo("has")
//    assertThat(pseudoAttribute.query.selectors).extracting("name").containsExactly("try", "exception")
//  }


}

