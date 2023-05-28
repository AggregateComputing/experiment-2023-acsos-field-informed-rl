package it.unibo.alchemist.boundary.swingui.effect.impl

import it.unibo.alchemist.boundary.swingui.effect.api.Effect
import it.unibo.alchemist.boundary.swingui.effect.impl.LearningEffect._
import it.unibo.alchemist.boundary.ui.api.Wormhole2D
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule
import it.unibo.alchemist.model.implementations.nodes._
import it.unibo.alchemist.model.interfaces.{Environment, Node, Position2D}
import org.danilopianini.lang.RangedInteger
import org.danilopianini.view.ExportForGUI

import java.awt._
import java.awt.geom._

/** ad-hoc effect to draw drones, animal and station. */
class LearningEffect extends Effect {

  @ExportForGUI(nameToExport = "Track")
  private val trackEnabled: Boolean = true

  @ExportForGUI(nameToExport = "SnapshotSize")
  private val snapshotSize: RangedInteger = new RangedInteger(10, MAX_LENGTH, LENGTH)

  @ExportForGUI(nameToExport = "SnapshotFrequency")
  private val timespan: RangedInteger = new RangedInteger(1, 100, CLOCK)

  @ExportForGUI(nameToExport = "NodeSize")
  private val nodeSize: RangedInteger = new RangedInteger(1, 20, DRONE_SIZE.toInt)

  @ExportForGUI(nameToExport = "Hue Molecule Property")
  private var colorMolecule: String = "hue"

  @ExportForGUI(nameToExport = "Velocity Molecule Property")
  private var velocityMolecule: String = "velocity"

  @ExportForGUI(nameToExport = "Max Value")
  private var maxValue: String = ""

  override def apply[T, P <: Position2D[P]](
      g: Graphics2D,
      node: Node[T],
      env: Environment[T, P],
      wormhole: Wormhole2D[P]
  ): Unit = {
    val nodePosition: P = env.getPosition(node)
    val viewPoint: Point = wormhole.getViewPoint(nodePosition)
    val (x, y) = (viewPoint.x, viewPoint.y)
    drawDirectedNode(g, node, x, y, env, wormhole)
  }

  def getColorSummary: Color = Color.BLACK

  def drawDirectedNode[T, P <: Position2D[P]](
      g: Graphics2D,
      node: Node[T],
      x: Int,
      y: Int,
      environment: Environment[T, P],
      wormhole: Wormhole2D[P]
  ): Unit = {
    val currentRotation = rotation(node)
    val transform = getTransform(x, y, nodeSize.getVal, currentRotation)
    val color = createColorOrBlack(node, environment)
    val transformedShape = transform.createTransformedShape(DRONE_SHAPE)
    g.setColor(color)
    g.fill(transformedShape)
  }

  private def rotation[T](node: Node[T]): Double = {
    val nodeManager = new SimpleNodeManager[T](node)
    val velocity = nodeManager.getOption(velocityMolecule).getOrElse(Array(1.0, 0.0))
    math.atan2(velocity(0), velocity(1))
  }

  private def getTransform(x: Int, y: Int, zoom: Double, rotation: Double): AffineTransform = {
    val transform = new AffineTransform()
    transform.translate(x, y)
    transform.scale(zoom, zoom)
    transform.rotate(rotation)
    transform
  }

  private def createColorOrBlack(node: Node[_], environment: Environment[_, _]): Color = {
    val currentMolecule = new SimpleMolecule(colorMolecule)
    if (node.contains(currentMolecule)) {
      val hue = node.getConcentration(currentMolecule).asInstanceOf[Number].doubleValue()
      val hueComponent = (hue.toFloat / maxValue.toDoubleOption.getOrElse(environment.getNodeCount.toDouble)).toFloat
      new Color(Color.HSBtoRGB(hueComponent, 1, 0.8f))
    } else {
      Color.BLACK
    }
  }
}

object LearningEffect {
  private val ADJUST_ALPHA_FACTOR: Int = 4

  private val CLOCK: Int = 10

  private val LENGTH: Int = 140

  private val MAX_LENGTH: Int = 1000

  private val MAX_COLOR: Double = 255

  private val DRONE_SHAPE: Ellipse2D.Double = new Ellipse2D.Double(-0.5, -0.5, 1, 1)

  private val DRONE_SIZE = 4.0
}
