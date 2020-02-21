package co.elpache.codelens.app.database


import co.elpache.codelens.tree.CodeTree
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

interface AstRepository : CrudRepository<AstRecord, String> {
  fun findByCommit(commit: String): AstRecord?
}

@Service
class AstStore {
  val mapper = ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(KotlinModule())

  private val logger = KotlinLogging.logger {}

  @Autowired
  lateinit var astRepository: AstRepository

  fun findByCommit(commit: String): AstRecord? = astRepository.findByCommit(commit)

  fun load(commit: String): CodeTree? {
    val record = astRepository.findByCommit(commit)
    return if (record != null) {
      logger.info { "DB hit" }
      mapper.readValue(record.ast, CodeTree::class.java)
    } else null
  }

  //Todo: This doesn't belong here
  fun save(commit: String, codeTree: CodeTree) {
    astRepository.save(AstRecord(commit, mapper.writeValueAsString(codeTree)))
  }

}