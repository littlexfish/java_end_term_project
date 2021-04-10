@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package hang_up_game.kotlin

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel
import hang_up_game.java.io.FileChecker
import hang_up_game.java.window.Open
import java.io.IOException
import java.net.URISyntaxException
import javax.swing.UIManager

fun main() {
	UIManager.setLookAndFeel(WindowsLookAndFeel())
	val openFrame = Open()
	openFrame.isVisible = true
	checkPlay(openFrame)
	
	
}

fun checkPlay(f: Open) {
	try {
		val fc = FileChecker()
		if(!fc.checkFiles()) {
			firstPlay(f, fc)
		}
	}
	catch(e: URISyntaxException) {
		e.printStackTrace()
	}
	loadGame(f)
}

fun firstPlay(f: Open, fc: FileChecker) {
	try {
		println("firstPlay")
		fc.createFiles()
	}
	catch(e: IOException) {
		e.printStackTrace()
	}
	f.dispose()
}

fun loadGame(f: Open) {
	if(f.isVisible) f.dispose()
}