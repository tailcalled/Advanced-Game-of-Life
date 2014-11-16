package advancedgol

import javax.swing.JPanel
import javax.swing.JFrame
import java.awt.Frame
import java.awt.BorderLayout
import javax.swing.JScrollPane
import java.awt.FlowLayout
import javax.swing.JButton
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.util.Random
import javax.swing.JToggleButton
import javax.swing.ButtonGroup

class GoL extends JPanel {
	
	private def a(f: => Unit): ActionListener = new ActionListener() {
		def actionPerformed(ev: ActionEvent) = {
			f
		}
	}
	
	object GoL extends Life(32, 32)
	object GoLPanel extends GoLPanel(GoL)
	object ButtonPanel extends JPanel(new FlowLayout())
	object Buttons {
		object Tick extends JButton("Tick") {
			addActionListener(a {
				GoL.tick()
				GoLPanel.repaint()
			})
		}
		private val group = new ButtonGroup
		class SpeedButton(hz: Int) extends JToggleButton(hz + " Hz") {
			addActionListener(a {
				updateRate = hz
			})
			group.add(this)
		}
		object Pause extends SpeedButton(0) { getModel().setSelected(true) }
		object Play extends SpeedButton(4)
		object FastForward extends SpeedButton(8)
		object Superspeed extends SpeedButton(32)
		object Clear extends JButton("Clear") {
			addActionListener(a {
				val r = new Random()
				for (x <- 0 until GoL.cells.w) {
					for (y <- 0 until GoL.cells.h) {
						GoL.cells(x, y) = new GoLCell(false)
					}
				}
			})
		}
		object Random extends JButton("Random") {
			addActionListener(a {
				val r = new Random()
				for (x <- 0 until GoL.cells.w) {
					for (y <- 0 until GoL.cells.h) {
						GoL.cells(x, y) = new GoLCell(r.nextBoolean())
					}
				}
			})
		}
	}
	object Tools extends GoLTools(GoLPanel)
	
	val autoupdate = new Thread() {
		override def run() {
			var time = System.currentTimeMillis()
			while (true) {
				while ((System.currentTimeMillis()-time) * updateRate > 1000) {
					time += 1000/updateRate
					GoL.tick()
				}
				GoLPanel.repaint()
				Thread.sleep(1000/60)
				if (updateRate == 0) time = System.currentTimeMillis()
			}
		}
	}
	
	var updateRate = 0
	
	setLayout(new BorderLayout)
	add(new JScrollPane(GoLPanel), BorderLayout.CENTER)
	add(ButtonPanel, BorderLayout.SOUTH)
	add(Tools, BorderLayout.EAST)
	
	ButtonPanel.add(Buttons.Tick)
	ButtonPanel.add(Buttons.Pause)
	ButtonPanel.add(Buttons.Play)
	ButtonPanel.add(Buttons.FastForward)
	ButtonPanel.add(Buttons.Superspeed)
	ButtonPanel.add(Buttons.Clear)
	ButtonPanel.add(Buttons.Random)
	
}

object GoL extends GoL {
	
	def main(args: Array[String]) = {
		val frame = new JFrame("Advanced Game of Life")
		frame.add(this)
		frame.setExtendedState(Frame.MAXIMIZED_BOTH)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		autoupdate.start()
		frame.setVisible(true)
	}
	
}