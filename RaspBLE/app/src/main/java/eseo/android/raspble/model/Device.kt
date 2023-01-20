package eseo.android.raspble.model

import android.bluetooth.BluetoothDevice

data class Device(
    var name: String?,
    var mac: String?,
    var device: BluetoothDevice? = null
) {
    override fun equals(other: Any?): Boolean {
        // On compare les MAC, pour ne pas ajouté deux fois le même device dans la liste.
        return other is Device && other.mac == this.mac
    }
}