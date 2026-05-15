package com.mindmatrix.kumbarakala

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaShaaleInventoryApp()
        }
    }
}

private enum class AssetCondition(val label: String, val color: Color, val icon: ImageVector) {
    Working("Working", Color(0xFF16803C), Icons.Rounded.CheckCircle),
    NeedsRepair("Needs Repair", Color(0xFFE59A00), Icons.Rounded.Warning),
    Broken("Broken", Color(0xFFD43D2F), Icons.Rounded.Error)
}

private data class SchoolAsset(
    val id: Int,
    val name: String,
    val serialNumber: String,
    val category: String,
    val condition: AssetCondition,
    val lastAuditDate: String,
    val note: String
)

private data class IssueLog(
    val assetName: String,
    val date: String,
    val reason: String
)

private val initialAssets = listOf(
    SchoolAsset(1, "Microscope", "LAB-203-A", "Lab Equipment", AssetCondition.Working, "15 May 2026", "Optics cleaned"),
    SchoolAsset(2, "Football Kit", "SPORT-118", "Sports Kit", AssetCondition.Broken, "15 May 2026", "Two balls missing"),
    SchoolAsset(3, "Learning Tablet", "TAB-044", "Tablet", AssetCondition.NeedsRepair, "14 May 2026", "Battery drains quickly"),
    SchoolAsset(4, "Science Model Set", "LAB-067", "Lab Equipment", AssetCondition.Working, "12 May 2026", "Complete set"),
    SchoolAsset(5, "Volleyball Net", "SPORT-072", "Sports Kit", AssetCondition.NeedsRepair, "11 May 2026", "Side rope torn")
)

@Composable
private fun NammaShaaleInventoryApp() {
    val context = LocalContext.current
    val assets = remember { mutableStateListOf<SchoolAsset>().apply { addAll(initialAssets) } }
    val issueLogs = remember {
        mutableStateListOf(
            IssueLog("Football Kit", "15 May 2026", "Football lost during match practice"),
            IssueLog("Learning Tablet", "14 May 2026", "Tablet battery needs service")
        )
    }

    var assetName by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Lab Equipment") }
    var note by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf(AssetCondition.Working) }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF2D2A7A),
            secondary = Color(0xFF0A7D73),
            background = Color(0xFFF5F7FB),
            surface = Color.White
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F7FB)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { AppHeader() }
                item { Dashboard(assets = assets) }
                item {
                    AssetRegister(
                        assetName = assetName,
                        serialNumber = serialNumber,
                        category = category,
                        note = note,
                        selectedCondition = selectedCondition,
                        onAssetNameChange = { assetName = it },
                        onSerialNumberChange = { serialNumber = it },
                        onCategoryChange = { category = it },
                        onNoteChange = { note = it },
                        onConditionChange = { selectedCondition = it },
                        onAddAsset = {
                            if (assetName.isBlank() || serialNumber.isBlank()) {
                                Toast.makeText(context, "Enter asset name and serial number", Toast.LENGTH_SHORT).show()
                            } else {
                                val conditionNote = note.ifBlank { "Monthly health check completed" }
                                val newAsset = SchoolAsset(
                                    id = (assets.maxOfOrNull { it.id } ?: 0) + 1,
                                    name = assetName.trim(),
                                    serialNumber = serialNumber.trim(),
                                    category = category,
                                    condition = selectedCondition,
                                    lastAuditDate = today(),
                                    note = conditionNote
                                )
                                assets.add(0, newAsset)
                                if (selectedCondition != AssetCondition.Working) {
                                    issueLogs.add(0, IssueLog(newAsset.name, newAsset.lastAuditDate, conditionNote))
                                }
                                assetName = ""
                                serialNumber = ""
                                note = ""
                                selectedCondition = AssetCondition.Working
                            }
                        }
                    )
                }
                item { SectionTitle(Icons.Rounded.Inventory2, "Asset Register") }
                items(assets, key = { it.id }) { asset ->
                    AssetRow(
                        asset = asset,
                        onConditionChange = { condition ->
                            val index = assets.indexOfFirst { it.id == asset.id }
                            if (index >= 0) {
                                val updated = asset.copy(condition = condition, lastAuditDate = today())
                                assets[index] = updated
                                if (condition != AssetCondition.Working) {
                                    issueLogs.add(0, IssueLog(updated.name, updated.lastAuditDate, updated.note))
                                }
                            }
                        }
                    )
                }
                item { IssueLogSection(issueLogs = issueLogs) }
                item { RepairRequestSection(assets = assets) }
                item {
                    Button(
                        onClick = { shareSummaryReport(context, assets, issueLogs) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A7D73))
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Share Summary Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF2D2A7A), Color(0xFF0A7D73))),
                RoundedCornerShape(8.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("MindMatrix VTU Internship Program", color = Color.White.copy(alpha = 0.88f), fontSize = 13.sp)
        Text("Namma-Shaale Inventory", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        Text(
            "Digital asset auditor for school kits, tablets, and lab equipment.",
            color = Color.White.copy(alpha = 0.92f),
            fontSize = 15.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun Dashboard(assets: List<SchoolAsset>) {
    val totalAssets = assets.size
    val working = assets.count { it.condition == AssetCondition.Working }
    val needsRepair = assets.count { it.condition == AssetCondition.NeedsRepair }
    val broken = assets.count { it.condition == AssetCondition.Broken }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(Icons.Rounded.Assessment, "Dashboard")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Total Assets", totalAssets.toString(), Color(0xFF2D2A7A), Modifier.weight(1f))
            MetricCard("Working", working.toString(), Color(0xFF16803C), Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MetricCard("Needs Repair", needsRepair.toString(), Color(0xFFE59A00), Modifier.weight(1f))
            MetricCard("Broken", broken.toString(), Color(0xFFD43D2F), Modifier.weight(1f))
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Color(0xFF687085), fontSize = 12.sp, maxLines = 1)
            Text(value, color = color, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetRegister(
    assetName: String,
    serialNumber: String,
    category: String,
    note: String,
    selectedCondition: AssetCondition,
    onAssetNameChange: (String) -> Unit,
    onSerialNumberChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onConditionChange: (AssetCondition) -> Unit,
    onAddAsset: () -> Unit
) {
    val categories = listOf("Lab Equipment", "Sports Kit", "Tablet", "Library", "Furniture")
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(Icons.Rounded.Add, "Add Asset")
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = assetName,
                    onValueChange = onAssetNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Item name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = serialNumber,
                    onValueChange = onSerialNumberChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Serial number / tag") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    onCategoryChange(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                ConditionPicker(selectedCondition = selectedCondition, onConditionChange = onConditionChange)
                OutlinedTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Audit note / issue reason") },
                    minLines = 2
                )
                Button(
                    onClick = onAddAsset,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Rounded.Inventory2, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Asset")
                }
            }
        }
    }
}

@Composable
private fun AssetRow(asset: SchoolAsset, onConditionChange: (AssetCondition) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ConditionDot(asset.condition)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(asset.name, color = Color(0xFF1E2433), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${asset.category} | ${asset.serialNumber}",
                        color = Color(0xFF687085),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(asset.condition.label, color = asset.condition.color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Text("Last health check: ${asset.lastAuditDate}", color = Color(0xFF4C566F), fontSize = 13.sp)
            Text(asset.note, color = Color(0xFF4C566F), fontSize = 13.sp)
            ConditionPicker(selectedCondition = asset.condition, onConditionChange = onConditionChange)
        }
    }
}

@Composable
private fun ConditionPicker(
    selectedCondition: AssetCondition,
    onConditionChange: (AssetCondition) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        AssetCondition.values().forEach { condition ->
            val selected = selectedCondition == condition
            OutlinedButton(
                onClick = { onConditionChange(condition) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) condition.color.copy(alpha = 0.12f) else Color.Transparent,
                    contentColor = condition.color
                )
            ) {
                Icon(condition.icon, contentDescription = null, modifier = Modifier.size(17.dp))
                Spacer(Modifier.width(4.dp))
                Text(condition.label, fontSize = 11.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun IssueLogSection(issueLogs: List<IssueLog>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(Icons.Rounded.ReportProblem, "Issue Log")
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (issueLogs.isEmpty()) {
                    Text("No issues recorded.", color = Color(0xFF687085))
                } else {
                    issueLogs.take(5).forEach { issue ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE3E7EF), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(issue.assetName, color = Color(0xFF1E2433), fontWeight = FontWeight.Bold)
                            Text("${issue.date} | ${issue.reason}", color = Color(0xFF687085), fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RepairRequestSection(assets: List<SchoolAsset>) {
    val repairs = assets.filter { it.condition != AssetCondition.Working }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(Icons.Rounded.Build, "Repair Requests")
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (repairs.isEmpty()) {
                    Text("All assets are working.", color = Color(0xFF16803C), fontWeight = FontWeight.Bold)
                } else {
                    repairs.forEach { asset ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ConditionDot(asset.condition)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "${asset.name} - ${asset.condition.label}",
                                color = Color(0xFF1E2433),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF2D2A7A), modifier = Modifier.size(21.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = Color(0xFF1E2433), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun ConditionDot(condition: AssetCondition) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(condition.color)
    )
}

private fun shareSummaryReport(context: Context, assets: List<SchoolAsset>, issueLogs: List<IssueLog>) {
    val report = buildSummaryReport(assets, issueLogs)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Namma-Shaale Inventory Summary")
        putExtra(Intent.EXTRA_TEXT, report)
    }
    context.startActivity(Intent.createChooser(intent, "Share Summary Report"))
}

private fun buildSummaryReport(assets: List<SchoolAsset>, issueLogs: List<IssueLog>): String {
    val repairItems = assets.filter { it.condition != AssetCondition.Working }
    return buildString {
        appendLine("Namma-Shaale Inventory Summary")
        appendLine("Date: ${today()}")
        appendLine()
        appendLine("Total Assets: ${assets.size}")
        appendLine("Working: ${assets.count { it.condition == AssetCondition.Working }}")
        appendLine("Needs Repair: ${assets.count { it.condition == AssetCondition.NeedsRepair }}")
        appendLine("Broken: ${assets.count { it.condition == AssetCondition.Broken }}")
        appendLine()
        appendLine("Repair Requests:")
        if (repairItems.isEmpty()) {
            appendLine("- None")
        } else {
            repairItems.forEach { appendLine("- ${it.name} (${it.serialNumber}): ${it.condition.label} - ${it.note}") }
        }
        appendLine()
        appendLine("Recent Issues:")
        if (issueLogs.isEmpty()) {
            appendLine("- None")
        } else {
            issueLogs.take(5).forEach { appendLine("- ${it.date}: ${it.assetName} - ${it.reason}") }
        }
    }
}

private fun today(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
}
