package co.elpache.codelens

import co.elpache.codelens.codetree.CodeFolder
import co.elpache.codelens.codetree.CodeTree

class Factory {
  //Todo: Move to property files or command line args
  var path = "../code-examples/"
  val repo = GitRepository(path, "https://github.com/ricardodpsx/test-repo.git")

  fun createBaseCode(): CodeTree {
    repo.init().goTo("master")
    return CodeTree()
      .expandFullCodeTree(CodeFolder.load(path))
      .applyAnalytics()
  }

  fun createBaseCode(commit: String): CodeTree {
    repo.init().goTo(commit)
    return CodeTree()
      .expandFullCodeTree(CodeFolder.load(path))
      .applyAnalytics()
  }
}