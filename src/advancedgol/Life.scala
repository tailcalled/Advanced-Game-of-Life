package advancedgol

class Life(w: Int, h: Int) {
	
	val cells: ArrayGrid[Cell] = new ArrayGrid[Cell](new GoLCell(), w, h)
	
	def tick() = {
		val count = new ArrayGrid(0, w, h)
		for (x <- 0 until w; y <- 0 until h) {
			if (cells(x, y).alive) {
				for (dx <- -1 until 2; dy <- -1 until 2; if dx != 0 || dy != 0) {
					count(x+dx, y+dy) += 1;
				}
			}
		}
		for (x <- 0 until w; y <- 0 until h) {
			cells(x, y).tick(count(x, y))
		}
	}
	
}