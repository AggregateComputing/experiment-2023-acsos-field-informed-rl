package it.unibo.alchemist

object Store {
  private var innerValues = Map("totalReward" -> 0.0)
  def values: Map[String, Double] = innerValues

  def store(key: String, value: Double): Unit =
    innerValues = innerValues + (key -> value)

}
