package co.elpache.codelens.app.database


import org.springframework.data.repository.CrudRepository

interface AstRepository : CrudRepository<AstRecord, String> {
  fun findByCommit(commit: String): AstRecord?
}