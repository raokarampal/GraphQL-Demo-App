import androidx.compose.ui.window.ComposeUIViewController
import com.droidslife.graphqldemo.App
import com.droidslife.graphqldemo.di.appModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

// Initialize Koin when the module is loaded
private val initKoin = startKoin {
    modules(appModule)
}

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
