package co.elpachecode.codelens.cssSelector

import co.elpache.codelens.CodeTree
import co.elpache.codelens.tree.Vid

data class CssSearch(val selectors: CssSelectors, val tree: CodeTree) {

  fun search(fromVertice: Vid = tree.rootVid()): List<Vid> {
    val found = arrayListOf<Vid>()
    dfs(fromVertice, buildSearchStateMachine(selectors.selectors, found))
    return found
  }

  private fun dfs(vid: Vid, currentState: SearchState) {
    val nextState = currentState.next(vid)
    for (cVid in tree.children(vid))
      dfs(cVid, nextState.copy())
  }

  private fun buildSearchStateMachine(selectors: List<CssSelector>, found: ArrayList<Vid>) = selectors
    .dropLast(1)
    .foldRight(buildChildState(selectors.last()) { found.add(it) })
    { t, next -> buildParentState(t, next) }


  private fun buildChildState(type: CssSelector, onMatch: (e: Vid) -> Unit) =
    when (type) {
      is TypeSelector -> TypeSelectorSearchState(tree, type) { onMatch(it) }
      is DescendantSelector -> DescendantSelectorSearchState(tree, type) { onMatch(it) }
      else -> error("Unsupported type ${type}")
    }

  private fun buildParentState(type: CssSelector, next: SearchState) =
    when (type) {
      is TypeSelector -> TypeSelectorSearchState(tree, type, next)
      is DescendantSelector -> DescendantSelectorSearchState(tree, type, next)
      else -> error("Unsupported type ${type}")
    }
}

interface SearchState {
  val tree: CodeTree
  val selector: CssSelector
  fun next(vid: Vid): SearchState
  fun copy(): SearchState
}

private class TypeSelectorSearchState(
  override val tree: CodeTree,
  override val selector: TypeSelector,
  val nextState: SearchState? = null,
  val onMatch: (vid: Vid) -> Unit = {}
) : SearchState {
  override fun copy() = this

  override fun next(vid: Vid): SearchState {
    if (matches(tree.v(vid).data(), selector)) {
      onMatch(vid)
      return nextState ?: this
    }
    return this
  }
}

private class DescendantSelectorSearchState(
  override val tree: CodeTree,
  override val selector: DescendantSelector,
  var nextState: SearchState? = null,
  val slidingWindow: SlidingWindow<Vid> = SlidingWindow(selector.descendants.size),
  val onMatch: (vid: Vid) -> Unit = {}
) : SearchState {

  fun nextState() = nextState ?: this

  override fun next(vid: Vid): SearchState {
    slidingWindow.addUntilFull(vid) { items ->
      if (matchAllDescendants(items)) {
        onMatch(slidingWindow.last())
        return nextState()
      }
    }
    return this
  }

  private fun matchAllDescendants(items: List<Vid>) =
    items.withIndex().all { matches(tree.v(it.value).data(), selector.descendants[it.index]) }

  override fun copy() = DescendantSelectorSearchState(tree, selector, nextState, slidingWindow.copy(), onMatch)
}

