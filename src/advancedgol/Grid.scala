package advancedgol

trait Grid[A] {
	
	def w: Int
	def h: Int
	
	def apply(x: Int, y: Int): A
	
}
class ArrayGrid[A] private (val arr: Array[Array[Any]]) extends Grid[A] {
	
	val w = arr.length
	val h = arr(0).length
	
	def this(default: => A, w: Int, h: Int) = this(Array.fill[Any](w, h)(default))
	
	def apply(x: Int, y: Int): A = 
		if (x < 0) apply(x+w, y)
		else if (y < 0) apply(x, y+h)
		else arr(x % w)(y % h).asInstanceOf[A]
	def update(x: Int, y: Int, v: A): Unit = 
		if (x < 0) update(x+w, y, v)
		else if (y < 0) update(x, y+h, v)
		else arr(x % w)(y % h) = v
	
}