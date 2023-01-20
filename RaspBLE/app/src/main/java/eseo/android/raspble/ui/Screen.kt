package eseo.android.raspble.ui

sealed class Screen(val route: String) {
    object Scan : Screen(route = "scan")
    object Connect : Screen(route = "connect")
    object Command : Screen(route = "command")
}
