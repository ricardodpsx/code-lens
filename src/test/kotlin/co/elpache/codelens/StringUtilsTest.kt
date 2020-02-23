package co.elpache.codelens

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

class StringUtilsTest {

  @Test
  fun testUnderscoreToCamel() {
    assertThat("A_B".underscoreToCamel()).isEqualTo("aB")
    assertThat("_B".underscoreToCamel()).isEqualTo("_b")
  }

  @Test
  fun `should find first quoted string`() {
    assertThat(Regex(QUOTED_STRING).find("'hello \\'world' abc]")?.value).isEqualTo("'hello \\'world'")
    assertThat(Regex(QUOTED_STRING).find("'hello \\'world\\'' abc]")?.value).isEqualTo("'hello \\'world\\''")
    assertThat(Regex(QUOTED_STRING).find("'hello \\' world' bua b")?.value).isEqualTo("'hello \\' world'")
    assertThat(Regex(QUOTED_STRING).find("'hello \" world \" aa' bua b 'another string'")?.value).isEqualTo("'hello \" world \" aa'")
    assertThat(Regex(QUOTED_STRING).find("""'something \'quoted\' and' something else too """)?.value).isEqualTo("'something \\'quoted\\' and'")
  }

  @Test
  fun `test file path`() {
    println(File("js/frontend/src/components/App.js").parentFile.resolve("../myStuff.js").normalize())
  }
}

