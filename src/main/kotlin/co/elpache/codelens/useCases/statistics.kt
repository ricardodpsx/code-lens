package co.elpache.codelens.useCases

import co.elpache.codelens.tree.Vid
import org.nield.kotlinstatistics.countBy
import org.nield.kotlinstatistics.descriptiveStatistics
import org.nield.kotlinstatistics.median

fun frequency(values: List<Pair<Vid, Double>>): List<ParamFrequencyRow> {
  val vidsByParam = HashMap<Double, List<Vid>>()

  values.forEach { p ->
    vidsByParam.computeIfAbsent(p.second) { listOf() }
    vidsByParam.computeIfPresent(p.second) { a: Double, b: List<Vid> ->
      b.plus(p.first)
    }
  }

  return values
    .countBy { it.second }
    .map { ParamFrequencyRow(it.key, it.value, vidsByParam[it.key] ?: listOf()) }
    .sortedBy { it.paramValue }
}

data class DescriptiveStatistics(
  val mean: Double,
  val median: Double,
  val std: Double,
  val min: Double,
  val max: Double,
  val quartiles: List<Double>
)

fun statistics(values: List<Pair<Vid, Int>>) =
  with(values.map { it.second }) {
    val ds = descriptiveStatistics

    if (ds.mean.isNaN()) {
      return@with DescriptiveStatistics(
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        listOf(0.0, 0.0, 0.0, 0.0)
      )
    }

    DescriptiveStatistics(
      mean = ds.mean,
      median = median(),
      std = ds.standardDeviation,
      min = ds.min,
      max = ds.max,
      quartiles = listOf(
        ds.percentile(25.0),
        ds.percentile(50.0),
        ds.percentile(75.0)
      )
    )
  }
