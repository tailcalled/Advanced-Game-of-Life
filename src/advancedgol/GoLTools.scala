package advancedgol

import javax.swing.JPanel
import javax.swing.JList
import javax.swing.JLabel
import javax.swing.JComponent
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.BoxLayout

sealed trait ToolAction
trait Tool {
	val action: ToolAction
	def settings: Option[JComponent]
}
object Tool {
	case class SelectCell(lbl: String, cont: (Int, Int) => ToolAction) extends ToolAction
	case class SetCell(x: Int, y: Int, newCell: Cell, cont: ToolAction) extends ToolAction
	case class GetCell(x: Int, y: Int, cont: Cell => ToolAction) extends ToolAction
	case object Noop extends ToolAction
	def simple(name: String, cell: => Cell): Tool = new Tool {
		override def toString() = name
		val action = SelectCell(name, (x, y) => SetCell(x, y, cell, Noop))
		def settings = None
	}
	val Alive = simple("Alive", new GoLCell(true))
	val Dead = simple("Dead", new GoLCell(false))
	val Wall = simple("Wall", new Wall(false))
	val Static = simple("Static", new Wall(true))
	val Portal: Tool = new Tool {
		override def toString() = "Portal"
		val action = SelectCell("Select source", (ix, iy) => SelectCell("Select destination", (ox, oy) => GetCell(ix, iy, (src) => {
			val dst = new Portal(src)
			SetCell(ix, iy, src, SetCell(ox, oy, dst, Noop))
		})))
		def settings = None
	}
	def tools = Seq(Alive, Dead, Wall, Static, Portal)
}

class GoLTools(life: GoLPanel, tools: Seq[Tool] = Tool.tools) extends JPanel {
	
	var panel: Option[JComponent] = None
	var currAction: ToolAction = null //todo: remove null
	var initialAction: ToolAction = null
	
	object ToolsLabel extends JLabel("Tools                                  ")
	object Action extends JLabel()
	
	def l(f: => Unit) = new ListSelectionListener() {
		def valueChanged(ev: ListSelectionEvent) = {
			f
		}
	}
	def m(f: (Int, Int) => Unit) = new MouseListener() {
		def mouseClicked(ev: MouseEvent) = {}
		def mousePressed(ev: MouseEvent) = {
			f(ev.getX(), ev.getY())
		}
		def mouseReleased(ev: MouseEvent) = {}
		def mouseEntered(ev: MouseEvent) = {}
		def mouseExited(ev: MouseEvent) = {}
	}
	
	object ToolList extends JList[Tool](tools.toArray) {
		addListSelectionListener(l {
			println(getSelectedValue())
			initialAction = getSelectedValue().action
			currAction = getSelectedValue().action
			runActions()
		})
	}
	
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
	add(ToolsLabel)
	add(Action)
	add(ToolList)
	
	life.addMouseListener(m { (x, y) => 
		println(currAction)
		currAction match {
		case Tool.SelectCell(_, cont) =>
			val gx = x/life.CellOffset
			val gy = y/life.CellOffset
			currAction = cont(gx, gy)
			runActions()
		case _ =>
		}
	})
	
	def runActions(): Unit = {
		while (true) {
			currAction match {
				case Tool.SetCell(x, y, c, cont) =>
					life.life.cells(x, y) = c
					currAction = cont
				case Tool.GetCell(x, y, cont) =>
					currAction = cont(life.life.cells(x, y))
				case Tool.Noop =>
					Action.setText("")
					Action.revalidate()
					currAction = initialAction
				case Tool.SelectCell(lbl, _) =>
					Action.setText(lbl)
					Action.revalidate()
					return
				case _ => return
			}
		}
	}
	
}