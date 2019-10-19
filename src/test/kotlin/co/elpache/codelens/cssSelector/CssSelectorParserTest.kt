package codelens.cssSelector

import co.elpachecode.codelens.cssSelector.RelationTypes
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.parseCssSelector
import org.assertj.core.api.Assertions.*
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
  fun `Select by numeric attribute`() {
    val type = parseCssSelector("myClass[lines=6]").selectors[0]
    assertThat(type.attributes[0].value).isEqualTo("6")
  }

  @Test
  fun cssSelectWithExtendedAttributeOperations() {
    val attr = parseCssSelector("class annotationEntry[first-name$='hello']").selectors[1].attributes[0]
    assertThat(attr.name).isEqualTo("first-name")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(attr.value).isEqualTo("hello")
  }

  @Test
  fun cssSelectWithMultipleAttributeSelectors() {
    val attrs = parseCssSelector("annotationEntry[first-name$='ricardo'][last-name='pacheco']").selectors[0].attributes
    assertThat(attrs[0].value).isEqualTo("ricardo")
    assertThat(attrs[1].value).isEqualTo("pacheco")
  }

  @Test
  fun cssSupportEscape() {
    val attr = parseCssSelector("class annotationEntry[first-name$='hello \\'world\\'']").selectors[1].attributes[0]
    assertThat(attr.name).isEqualTo("first-name")
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


}

