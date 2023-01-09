package eseo.android.raspble.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eseo.android.raspble.model.Device
import eseo.android.raspble.ui.Screen
import eseo.android.raspble.ui.components.ClickableButton
import eseo.android.raspble.ui.components.Device4Recycler

// Gestion du Bluetooth

private val PERMISSION_REQUEST_LOCATION = 1

// L'Adapter permettant de se connecter
private var bluetoothAdapter: BluetoothAdapter? = null

// La connexion actuellement établie
private var currentBluetoothGatt: BluetoothGatt? = null

// « Interface système nous permettant de scanner »
private var bluetoothLeScanner: BluetoothLeScanner? = null

// Parametrage du scan BLE
private val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

// On ne retourne que les « Devices » proposant le bon UUID
private var scanFilters: List<ScanFilter> = arrayListOf(
//        ScanFilter.Builder().setServiceUuid(ParcelUuid(BluetoothLEManager.DEVICE_UUID)).build()
)

// Variable de fonctionnement
private var mScanning = false
private val handler = Handler(Looper.getMainLooper())


// DataSource de notre adapter.
private val bleDevicesFoundList = arrayListOf<Device>()

@Composable
fun getContext(): Context {
    return LocalContext.current
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ConnectScreen(
    navController: NavController
) {
    var onClickPermission by remember { mutableStateOf(false) }
    if (onClickPermission) {
        if (hasPermission()) {
            setupBLE()
        } else {
            askForPermission()
            onRequestPermissionsResult(
                requestCode = PERMISSION_REQUEST_LOCATION,
                permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
            )
        }
        onClickPermission = false
    }

    val myList = listOf(
        Device("Device 1", "00:00:00:00:00:00"),
        Device("Device 2", "00:00:00:00:00:00"),
        Device("Device 3", "00:00:00:00:00:00"),
        Device("Device 4", "00:00:00:00:00:00"),
        Device("Device 5", "00:00:00:00:00:00"),
        Device("Device 6", "00:00:00:00:00:00"),
        Device("Device 7", "00:00:00:00:00:00"),
        Device("Device 8", "00:00:00:00:00:00"),
        Device("Device 9", "00:00:00:00:00:00"),
        Device("Device 10", "00:00:00:00:00:00"),
        Device("Device 11", "00:00:00:00:00:00"),
        Device("Device 12", "00:00:00:00:00:00"),
        Device("Device 13", "00:00:00:00:00:00"),
        Device("Device 14", "00:00:00:00:00:00"),
        Device("Device 15", "00:00:00:00:00:00"),
    )

    val deviceList by remember { mutableStateOf(emptyArray<Device>()) }

    Scaffold {
        LazyColumn {
            items(deviceList) { item -> Device4Recycler(item.name!!, item.mac!!) }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableButton(
                text = "TOGGLE LED",
                onClick = { navController.navigate(Screen.Command.route) }
            )
            Row {
                ClickableButton(
                    text = "LANCER LE SCAN",
                    onClick = { onClickPermission = true }
                )
                Spacer(modifier = Modifier.width(10.dp))
                ClickableButton(
                    text = "DECONNEXION",
                    onClick = { navController.navigate(Screen.Scan.route) }
                )
            }
        }
    }
}

/* ================= SCAN ===================== */

/**
 * Gère l'action après la demande de permission.
 * 2 cas possibles :
 * - Réussite 🎉.
 * - Échec (refus utilisateur).
 */
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

    if (requestCode == PERMISSION_REQUEST_LOCATION) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && locationServiceEnabled()) {
            setupBLE()
        } else if (!locationServiceEnabled()) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        } else {
            // Permission KO => Gérer le cas.
            // Vous devez ici modifier le code pour gérer le cas d'erreur (permission refusé)
            // Avec par exemple une Dialog
            Toast.makeText(getContext(), "Permission refusée, acceptez svp", Toast.LENGTH_SHORT).show()
        }
    }
}

/**
 * Permet de vérifier si l'application possede la permission « Localisation ». OBLIGATOIRE pour scanner en BLE
 */
@Composable
private fun hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(getContext(),
            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
            Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * Demande de la permission (ou des permissions) à l'utilisateur.
 */
@Composable
private fun askForPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        ActivityCompat.requestPermissions(getContext() as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_LOCATION)
    } else {
        ActivityCompat.requestPermissions(getContext() as Activity,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN),
            PERMISSION_REQUEST_LOCATION)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun locationServiceEnabled(): Boolean {
        val lm = getContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isLocationEnabled
    }

/**
 * Récupération de l'adapter Bluetooth & vérification si celui-ci est actif
 */
@Composable
@SuppressLint("MissingPermission")
private fun setupBLE() {
    (getContext().getSystemService(BLUETOOTH_SERVICE) as BluetoothManager?)?.let { bluetoothManager ->
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter != null && !bluetoothManager.adapter.isEnabled) {
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    // Le Bluetooth est activé, on lance le scan
                    scanLeDevice()
                } else {
                    // Bluetooth non activé
                }
            }.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } else {
            scanLeDevice()
        }
    }
}

// Le scan va durer 10 secondes seulement, sauf si vous passez une autre valeur comme paramètre.
@SuppressLint("MissingPermission")
private fun scanLeDevice(scanPeriod: Long = 10000) {
    if (!mScanning) {
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner

        // On vide la liste qui contient les devices actuellement trouvés
        bleDevicesFoundList.clear()

        mScanning = true

        // On lance une tache qui durera « scanPeriod » à savoir donc de base
        // 10 secondes
        handler.postDelayed({
            mScanning = false
            bluetoothLeScanner?.stopScan(leScanCallback)
        }, scanPeriod)

        // On lance le scan
        bluetoothLeScanner?.startScan(scanFilters, scanSettings, leScanCallback)
    }
}

// Callback appelé à chaque périphérique trouvé.
private val leScanCallback: ScanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)

        // C'est ici que nous allons créer notre « Device » et l'ajouter dans la dataSource de notre RecyclerView

//         val device = Device(result.device.name, result.device.address, result.device)
//         if (!device.name.isNullOrBlank() && !bleDevicesFoundList.contains(device)) {
//             bleDevicesFoundList.add(device)
//             Indique à l'adapter que nous avons ajouté un élément, il va donc se mettre à jour
//             findViewById<RecyclerView>(R.id.rvDevices).adapter?.notifyItemInserted(bleDevicesFoundList.size - 1)
//         }
    }
}

/* ================= END SCAN ===================== */

@RequiresApi(Build.VERSION_CODES.P)
@Composable
@Preview(showBackground = true)
fun ConnectScreenPreview() {
    ConnectScreen(navController = rememberNavController())
}