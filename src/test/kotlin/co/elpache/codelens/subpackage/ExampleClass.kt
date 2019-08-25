package codelens.subpackage

import java.io.File
import java.lang.RuntimeException
import java.math.BigDecimal


@AnAnnotation
class ExampleClass(val aProperty: Number) {

  fun exampleMethod() {
    val f = File(".")
    val bd = BigDecimal("1")

    val a = 1;
    val b = 2;

    var max = a

    if (a > b) {
      max = a
    } else {
      max = b
    }
  }
}

fun a() {
  try {
    System.out.println("Hello world")
  } catch (e : RuntimeException) {
    System.out.println("Error world")
  }
}

fun methodWithSixLines() {
  println("1")
  println("2")
  println("3")
  println("4")
}

fun myFunction1(x: String, y: Int) {

}


annotation class AnAnnotation {
}