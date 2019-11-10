package co.elpache.codelens

import co.elpache.codelens.app.database.AstRepository
import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree
import co.elpache.codelens.tree.Tree
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.springframework.context.ApplicationContext

class Factory(
  val path: String = "./tmp", //Path to put the repository and navigate back and forth
  val currentCodePath: String = "../code-examples",
  val repoUrl: String = "https://github.com/ricardodpsx/test-repo.git",
  val context: ApplicationContext? = null
) {
  private val logger = KotlinLogging.logger {}

  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {

    val codeTree = CodeTree()
      .expandFullCodeTree(CodeFolder.load(currentCodePath))
      .applyAnalytics()

    logger.info { "Done loading code" }

    return codeTree
  }

  fun getAstDatabase() = context!!.getBean(AstRepository::class.java)

  fun createBaseCode(tree: Tree): CodeTree {
    val ct = CodeTree(tree)
    return ct
  }

  val mapper = ObjectMapper().registerModule(KotlinModule())

  val astCache = HashMap<String, CodeTree>()

  fun createBaseCode(commit: String): CodeTree {
    //In memory cache
    if (astCache[commit] != null) {
      logger.info { "in Memory hit" }
      return astCache[commit]!!
    }

    //Database cache
    val record = getAstDatabase().findByCommit(commit)

    val res = if (record != null)
      createBaseCode(mapper.readValue(record.ast, Tree::class.java))
    else {
      repo.goTo(commit)
      CodeTree()
        .expandFullCodeTree(CodeFolder.load(path)).applyAnalytics()
    }
    astCache[commit] = res
    return res
  }

}