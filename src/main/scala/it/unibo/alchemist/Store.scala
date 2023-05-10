package it.unibo.alchemist

object Store {
  private var innerValues = Map.empty[String, Double]
  def values: Map[String, Double] = innerValues

  def store(key: String, value: Double): Unit =
    innerValues = innerValues + (key -> value)

}
