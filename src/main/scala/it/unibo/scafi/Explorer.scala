package it.unibo.scafi

class Explorer extends Agent {
  override def main(): Any = {
    val state = computeState()
    node.put("hue", mid()) // for visualization
    node.put("state", state)
    node.put("action", policy(state))
  }
}
