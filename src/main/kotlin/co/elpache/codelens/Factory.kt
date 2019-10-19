package co.elpache.codelens

import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree

class Factory(
  val path: String = "../code-examples/",
  val codePath: String = path,
  repoUrl: String = "https://github.com/ricardodpsx/test-repo.git"
  ) {
  //Todo: Move to property files or command line args
  val repo = GitRepository(path, repoUrl)

  fun createBaseCode(): CodeTree {
    repo.init().goTo("master")
    return CodeTree()
      .expandFullCodeTree(CodeFolder.load(codePath))
      .applyAnalytics()
  }

  fun createBaseCode(commit: String): CodeTree {
    repo.init().goTo(commit)

    return CodeTree()
      .expandFullCodeTree(CodeFolder.load(codePath))
      .applyAnalytics()
  }
}