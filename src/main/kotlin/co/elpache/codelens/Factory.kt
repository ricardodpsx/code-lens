package co.elpache.codelens

import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree
import mu.KotlinLogging

class Factory(
  val path: String = "../code-examples/",
  val codePath: String = path,
  val repoUrl: String = "https://github.com/ricardodpsx/test-repo.git"
) {
  private val logger = KotlinLogging.logger {}

  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {

    logger.info { "Loading base into $path ($codePath) from repo $repoUrl" }
    repo.init().goTo("master")

    val codeTree = CodeTree()
      .expandFullCodeTree(CodeFolder.load(codePath))
      .applyAnalytics()

    logger.info { "Done loading code" }

    return codeTree
  }

  fun createBaseCode(commit: String): CodeTree {
    repo.init().goTo(commit)

    return CodeTree()
      .expandFullCodeTree(CodeFolder.load(codePath))
      .applyAnalytics()
  }
}