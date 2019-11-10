package co.elpache.codelens.app.database


import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class AstRecord(
  @Id
  val commit: String? = null,
  @Lob
  val ast: String = ""
) {

  override fun toString() = "$commit\n$ast"
}