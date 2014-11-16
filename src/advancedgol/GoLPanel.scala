package advancedgol

import javax.swing.JComponent
import java.awt.Graphics
import java.awt.Color

class GoLPanel(val life: Life) extends JComponent {
	
	private def CellSpace = getWidth.toDouble / life.cells.w min getHeight.toDouble / life.cells.h
	def CellOffset = CellSpace.floor.toInt
	def CellSize = CellOffset - 1
	
	override def paintComponent(gfx: Graphics) {
		gfx.setColor(Color.WHITE)
		gfx.fillRect(0, 0, getWidth, getHeight)
		for (x <- 0 until life.cells.w; y <- 0 until life.cells.h) {
			val tCol = (life.cells(x, y).getClass().getSimpleName().hashCode() % 1000).abs.toFloat / 1000
			val qCol = (life.cells(x, y).getClass().hashCode() % 1000).abs.toFloat / 1000
			val lCol = if (life.cells(x, y).alive) 1.0f else 0.2f
			println(life.cells(x, y).getClass().getName() + life.cells(x, y).getClass().getName().hashCode() % 1000)
			val c = new Color(1 - lCol, 1, tCol)
			gfx.setColor(Color.getHSBColor(Math.PI.toFloat * tCol, lCol, 1.0f))
			gfx.fillRect(x * CellOffset, y * CellOffset, CellSize, CellSize)
		}
	}
	
}