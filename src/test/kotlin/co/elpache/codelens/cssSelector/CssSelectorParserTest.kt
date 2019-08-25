package codelens.cssSelector

import co.elpachecode.codelens.cssSelector.DescendantSelector
import co.elpachecode.codelens.cssSelector.TypeSelector
import co.elpachecode.codelens.cssSelector.parseCssSelector
import org.assertj.core.api.Assertions.*
import org.junit.Test

class CssSelectorParserTest {

  @Test
  fun cssParserSimpleSelect() {
    val root = parseCssSelector("class annotationEntry")

    assertThat((root.selectors[0] as TypeSelector).name).isEqualTo("class")
    assertThat((root.selectors[1] as TypeSelector).name).isEqualTo("annotationEntry")
  }

  @Test
  fun cssSelectWithAttributes() {
    val attr = (parseCssSelector("class annotationEntry[name]").selectors[1] as TypeSelector).attributes[0]
    assertThat(attr.name).isEqualTo("name")

    val attrWithFilter = (parseCssSelector("class annotationEntry[name='hello']").selectors[1] as TypeSelector).attributes[0]

    assertThat(attrWithFilter.name).isEqualTo("name")
    assertThat(attrWithFilter.op).isEqualTo("=")
    assertThat(attrWithFilter.value).isEqualTo("hello")
  }

  @Test
  fun cssSelectWithExtendedAttributeOperations() {
    val attr = (parseCssSelector("class annotationEntry[first-name$='hello']").selectors[1] as TypeSelector).attributes[0]
    assertThat(attr.name).isEqualTo("first-name")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(attr.value).isEqualTo("hello")
  }

  @Test
  fun cssSelectWithMultipleAttributeSelectors() {
    val attrs = (parseCssSelector("annotationEntry[first-name$='ricardo'][last-name='pacheco']").selectors[0] as TypeSelector).attributes
    assertThat(attrs[0].value).isEqualTo("ricardo")
    assertThat(attrs[1].value).isEqualTo("pacheco")
  }

  @Test
  fun cssSupportEscape() {
    val attr = (parseCssSelector("class annotationEntry[first-name$='hello \\'world\\'']").selectors[1] as TypeSelector).attributes[0]
    assertThat(attr.name).isEqualTo("first-name")
    assertThat(attr.op).isEqualTo("$=")
    assertThat(attr.value).isEqualTo("hello \\'world\\'")
  }

  @Test
  fun cssDirectChildSelector() {
    val type = parseCssSelector("class>method").selectors[0] as DescendantSelector
    assertThat(type.descendants[0].name).isEqualTo("class")
    assertThat(type.descendants[1].name).isEqualTo("method")
  }

}

