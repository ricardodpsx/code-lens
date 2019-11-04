package co.elpache.codelens.useCases

import co.elpache.codelens.tree.Vid
import org.nield.kotlinstatistics.countBy
import org.nield.kotlinstatistics.descriptiveStatistics
import org.nield.kotlinstatistics.median


fun frequency(values: List<Pair<Vid, Int>>) =
  values
    .countBy { it.second }
    .map { listOf(it.key, it.value) }
    .sortedBy { it[0] }

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
