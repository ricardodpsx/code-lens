package co.elpachecode.codelens.cssSelector

import java.util.Deque
import java.util.LinkedList

class SlidingWindow<T>(val size: Int, val window: Deque<T> = LinkedList()) {

  fun add(item: T) {
    if (window.size < size) window.addLast(item)
  }

  inline fun addUntilFull(item: T, c: (List<T>) -> Unit) {
    add(item)
    if (window.size == size) c(slide())
  }

  fun slide(): List<T> {
    val copy = window.toList()
    window.removeFirst()
    return copy
  }

  fun last() = window.last
  fun toList() = window.toList()
  fun copy() = SlidingWindow(size, LinkedList(window))
}