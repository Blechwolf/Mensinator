package com.mensinator.app

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream

@Composable
fun ExportImportDialog(
    onDismissRequest: () -> Unit,
    onExportClick: (String) -> Unit,
    onImportClick: (String) -> Unit
) {
    val context = LocalContext.current
    val exportImport = remember { ExportImport() }

    val exportPath = remember { mutableStateOf(exportImport.getDocumentsExportFilePath()) }
    val importPath = remember { mutableStateOf("") }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(ExportImport().getDefaultImportFilePath(context))
            val outputStream = FileOutputStream(file)
            try {
                inputStream?.copyTo(outputStream)
                importPath.value = file.absolutePath
                // Call the import function
                onImportClick(importPath.value)
                // Show success toast
                Toast.makeText(context, "File was imported successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Show error toast
                Toast.makeText(context, "Failed to import file", Toast.LENGTH_SHORT).show()
                Log.d("ExportImportDialog", "Failed to import file: ${e.message}, ${e.stackTraceToString()}")
            } finally {
                // Clean up
                inputStream?.close()
                outputStream.close()
            }
            // Dismiss the dialog after importing
            onDismissRequest()
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                onExportClick(exportPath.value)
                Toast.makeText(context, "Exported to ${exportPath.value}", Toast.LENGTH_SHORT).show()
                onDismissRequest()
            }) {
                Text("Export")
            }
        },
        dismissButton = {
            Button(onClick = {
                importLauncher.launch("application/json")
            }) {
                Text("Import")
            }
        },
        title = {
            Text("Export/Import Data")
        },
        text = {
            Column {
                Text("Export Path: ${exportPath.value}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Import Path: ${importPath.value}")
            }
        }
    )
}