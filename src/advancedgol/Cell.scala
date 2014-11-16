package advancedgol

trait Cell {
	def alive: Boolean
	def tick(neighbours: Int): Unit
}
class GoLCell(var alive: Boolean) extends Cell {
	
	def this() = this(false)
	
	def tick(neighbours: Int) =
		if (neighbours != 2) {
			if (neighbours == 3) alive = true
			else alive = false
		}
	
}
class Wall(val alive: Boolean) extends Cell {
	
	def tick(neighbours: Int) = {}
	
}
class Portal(val cell: Cell) extends Cell {
	
	def alive = cell.alive
	def tick(neighbours: Int) = {}
	
}