package com.mindmatrix.kumbarakala

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas as AndroidCanvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocalDining
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KumbaraKalaApp() }
    }
}

private data class ClayItem(
    val name: String,
    val type: String,
    val benefit: String,
    val ecoTip: String,
    val color: Color,
    val icon: ImageVector
)

private data class CustomerReview(
    val customerName: String,
    val itemName: String,
    val rating: Int,
    val reviewText: String,
    val photoUri: String?
)

private val clayItems = listOf(
    ClayItem(
        name = "Curd Pot",
        type = "Kitchen Heritage",
        benefit = "Maintains natural pH balance and keeps curd cool without electricity.",
        ecoTip = "A reusable clay pot replaces plastic storage and supports local soil craft.",
        color = Color(0xFFB75F32),
        icon = Icons.Rounded.WaterDrop
    ),
    ClayItem(
        name = "Clay Water Matka",
        type = "Natural Cooling",
        benefit = "Cools drinking water gently through natural evaporation.",
        ecoTip = "No refrigerator load, no plastic bottle habit, and better summer hydration.",
        color = Color(0xFF0F7A78),
        icon = Icons.Rounded.Eco
    ),
    ClayItem(
        name = "Handmade Diya",
        type = "Festival Light",
        benefit = "Brings traditional handmade warmth to homes and celebrations.",
        ecoTip = "Biodegradable lighting decor made by village artisans.",
        color = Color(0xFFC58B22),
        icon = Icons.Rounded.Image
    ),
    ClayItem(
        name = "Cooking Handi",
        type = "Slow Cooking",
        benefit = "Adds earthy aroma and keeps food naturally mineral-rich.",
        ecoTip = "Clay cookware is long-lasting, non-toxic, and plastic-free.",
        color = Color(0xFF7E3F24),
        icon = Icons.Rounded.LocalDining
    )
)

@Composable
private fun KumbaraKalaApp() {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(clayItems.first()) }
    var artisanName by remember { mutableStateOf("Lakshmi Kumbara") }
    var phoneNumber by remember { mutableStateOf("98765 43210") }
    var village by remember { mutableStateOf("Ramanagara, Karnataka") }
    val customerReviews = remember { mutableStateListOf<CustomerReview>() }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF7E3F24),
            secondary = Color(0xFF0F7A78),
            background = Color(0xFFFFFAF3),
            surface = Color.White
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFFFAF3)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Header() }
                item { SectionTitle(Icons.Rounded.Storefront, "Catalog") }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(clayItems) { item ->
                            CatalogCard(
                                item = item,
                                selected = selectedItem == item,
                                onClick = { selectedItem = item }
                            )
                        }
                    }
                }
                item { SectionTitle(Icons.Rounded.AutoStories, "Story Generator") }
                item {
                    StoryCardPreview(
                        item = selectedItem,
                        artisanName = artisanName,
                        phoneNumber = phoneNumber,
                        village = village
                    )
                }
                item { SectionTitle(Icons.Rounded.AccountCircle, "Artisan Bio") }
                item {
                    ArtisanForm(
                        artisanName = artisanName,
                        phoneNumber = phoneNumber,
                        village = village,
                        onArtisanNameChange = { artisanName = it },
                        onPhoneNumberChange = { phoneNumber = it },
                        onVillageChange = { village = it }
                    )
                }
                item { SectionTitle(Icons.Rounded.RateReview, "Customer Reviews") }
                item {
                    ReviewForm(
                        selectedItem = selectedItem,
                        onAddReview = { review -> customerReviews.add(0, review) }
                    )
                }
                items(customerReviews) { review ->
                    ReviewCard(review = review)
                }
                item {
                    Button(
                        onClick = {
                            shareStoryCard(
                                context = context,
                                item = selectedItem,
                                artisanName = artisanName,
                                phoneNumber = phoneNumber,
                                village = village
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F7A78))
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Share Story Card", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF7E3F24), Color(0xFFC58B22))),
                RoundedCornerShape(8.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("MindMatrix VTU Internship Program", color = Color.White.copy(alpha = 0.88f), fontSize = 13.sp)
        Text("Kumbara-Kala", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text(
            "Legacy branding for potters: catalog clay products, generate benefit cards, and share them with buyers.",
            color = Color.White.copy(alpha = 0.94f),
            fontSize = 15.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun CatalogCard(item: ClayItem, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(184.dp)
            .clickable(onClick = onClick)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color(0xFF0F7A78) else Color(0xFFE6D8C6),
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PotteryArt(item = item, modifier = Modifier.fillMaxWidth().height(96.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(7.dp))
                Text(item.type, color = Color(0xFF6F5B4C), fontSize = 12.sp, maxLines = 1)
            }
            Text(item.name, color = Color(0xFF2D2018), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(item.benefit, color = Color(0xFF6F5B4C), fontSize = 12.sp, lineHeight = 17.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun PotteryArt(item: ClayItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF1DD)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(item.color.copy(alpha = 0.16f), radius = size.minDimension * 0.46f, center = center)
            drawLine(
                color = item.color.copy(alpha = 0.35f),
                start = Offset(0f, size.height * 0.75f),
                end = Offset(size.width, size.height * 0.58f),
                strokeWidth = 5f
            )
            drawOval(
                color = item.color,
                topLeft = Offset(size.width * 0.28f, size.height * 0.23f),
                size = Size(size.width * 0.44f, size.height * 0.58f)
            )
            drawOval(
                color = Color(0xFFFFD9AA),
                topLeft = Offset(size.width * 0.34f, size.height * 0.25f),
                size = Size(size.width * 0.32f, size.height * 0.14f)
            )
            drawOval(
                color = Color(0xFF4B2619),
                topLeft = Offset(size.width * 0.38f, size.height * 0.29f),
                size = Size(size.width * 0.24f, size.height * 0.07f)
            )
            drawPath(
                path = Path().apply {
                    moveTo(size.width * 0.34f, size.height * 0.56f)
                    quadraticTo(size.width * 0.50f, size.height * 0.66f, size.width * 0.66f, size.height * 0.56f)
                },
                color = Color.White.copy(alpha = 0.55f),
                style = Stroke(width = 4f)
            )
        }
    }
}

@Composable
private fun StoryCardPreview(item: ClayItem, artisanName: String, phoneNumber: String, village: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.18f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFFFE1B7), Color(0xFFFFFAF3))))
                    .border(1.dp, Color(0xFFE2C199), RoundedCornerShape(8.dp))
                    .padding(18.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    repeat(7) { index ->
                        drawCircle(
                            color = item.color.copy(alpha = 0.09f),
                            radius = 48f + index * 10f,
                            center = Offset(size.width * 0.82f, size.height * 0.18f)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Kumbara-Kala", color = Color(0xFF7E3F24), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(item.name, color = Color(0xFF2D2018), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    PotteryArt(item = item, modifier = Modifier.fillMaxWidth().height(128.dp))
                    Text("Health Benefit", color = Color(0xFF0F7A78), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(item.benefit, color = Color(0xFF38251A), fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)
                    Text(item.ecoTip, color = Color(0xFF6F5B4C), fontSize = 13.sp, lineHeight = 18.sp)
                    Spacer(Modifier.height(2.dp))
                    Text("By $artisanName | $village", color = Color(0xFF2D2018), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("WhatsApp: $phoneNumber", color = Color(0xFF0F7A78), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                "Generated card is designed for WhatsApp sharing with readable benefit text and artisan contact details.",
                color = Color(0xFF6F5B4C),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ArtisanForm(
    artisanName: String,
    phoneNumber: String,
    village: String,
    onArtisanNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onVillageChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = artisanName,
                onValueChange = onArtisanNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Potter name") },
                singleLine = true
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone / WhatsApp number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            OutlinedTextField(
                value = village,
                onValueChange = onVillageChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Village / location") },
                singleLine = true
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE1B7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.AccountCircle, contentDescription = null, tint = Color(0xFF7E3F24))
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "Meet the maker: $artisanName carries forward clay craft from $village.",
                    color = Color(0xFF6F5B4C),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ReviewForm(
    selectedItem: ClayItem,
    onAddReview: (CustomerReview) -> Unit
) {
    val context = LocalContext.current
    var customerName by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFF1DD))
                        .border(1.dp, Color(0xFFE2C199), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri == null) {
                        Icon(Icons.Rounded.AddAPhoto, contentDescription = null, tint = Color(0xFF7E3F24))
                    } else {
                        ReviewPhoto(uri = photoUri.toString(), modifier = Modifier.fillMaxSize())
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Used ${selectedItem.name}", color = Color(0xFF2D2018), fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { photoPicker.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E3F24))
                    ) {
                        Icon(Icons.Rounded.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(if (photoUri == null) "Add Pot Photo" else "Change Photo")
                    }
                }
            }
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Customer name") },
                singleLine = true
            )
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Review after using the pot") },
                minLines = 3
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                (1..5).forEach { value ->
                    Button(
                        onClick = { rating = value },
                        modifier = Modifier.weight(1f).height(42.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (rating >= value) Color(0xFFC58B22) else Color(0xFFE6D8C6),
                            contentColor = if (rating >= value) Color.White else Color(0xFF7E3F24)
                        )
                    ) {
                        Icon(Icons.Rounded.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Button(
                onClick = {
                    if (photoUri == null || customerName.isBlank() || reviewText.isBlank()) {
                        Toast.makeText(context, "Add photo, name, and review", Toast.LENGTH_SHORT).show()
                    } else {
                        onAddReview(
                            CustomerReview(
                                customerName = customerName.trim(),
                                itemName = selectedItem.name,
                                rating = rating,
                                reviewText = reviewText.trim(),
                                photoUri = photoUri.toString()
                            )
                        )
                        customerName = ""
                        reviewText = ""
                        rating = 5
                        photoUri = null
                        Toast.makeText(context, "Review added", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F7A78))
            ) {
                Icon(Icons.Rounded.RateReview, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Customer Review", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: CustomerReview) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF1DD)),
                contentAlignment = Alignment.Center
            ) {
                if (review.photoUri == null) {
                    Icon(Icons.Rounded.Image, contentDescription = null, tint = Color(0xFF7E3F24))
                } else {
                    ReviewPhoto(uri = review.photoUri, modifier = Modifier.fillMaxSize())
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(review.itemName, color = Color(0xFF2D2018), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (index < review.rating) Color(0xFFC58B22) else Color(0xFFE6D8C6),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text("By ${review.customerName}", color = Color(0xFF0F7A78), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(review.reviewText, color = Color(0xFF6F5B4C), fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun ReviewPhoto(uri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageBitmap = remember(uri) {
        runCatching {
            context.contentResolver.openInputStream(Uri.parse(uri))?.use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        }.getOrNull()
    }

    if (imageBitmap == null) {
        Icon(Icons.Rounded.Image, contentDescription = null, tint = Color(0xFF7E3F24))
    } else {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Customer pot photo",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun SectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF7E3F24), modifier = Modifier.size(21.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = Color(0xFF2D2018), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
    }
}

private fun shareStoryCard(
    context: Context,
    item: ClayItem,
    artisanName: String,
    phoneNumber: String,
    village: String
) {
    try {
        val uri = createStoryCardImage(context, item, artisanName, phoneNumber, village)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Story Card"))
    } catch (exception: Exception) {
        Toast.makeText(context, "Unable to share card: ${exception.message}", Toast.LENGTH_LONG).show()
    }
}

private fun createStoryCardImage(
    context: Context,
    item: ClayItem,
    artisanName: String,
    phoneNumber: String,
    village: String
): Uri {
    val bitmap = Bitmap.createBitmap(1080, 1350, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)
    val width = bitmap.width.toFloat()
    val height = bitmap.height.toFloat()
    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(0f, 0f, width, height, 0xFFFFE1B7.toInt(), 0xFFFFFAF3.toInt(), Shader.TileMode.CLAMP)
    }
    canvas.drawRect(0f, 0f, width, height, bgPaint)

    val clayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = item.color.toArgbInt() }
    val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFD9AA.toInt() }
    val darkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF4B2619.toInt() }
    canvas.drawRoundRect(RectF(70f, 70f, width - 70f, height - 70f), 38f, 38f, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFFFFFFF.toInt() })
    canvas.drawRoundRect(RectF(94f, 94f, width - 94f, height - 94f), 30f, 30f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = 0xFFE2C199.toInt()
    })
    canvas.drawCircle(width * 0.78f, height * 0.19f, 210f, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = item.color.copy(alpha = 0.13f).toArgbInt() })

    drawTextLine(canvas, "Kumbara-Kala", 122f, 172f, 38f, 0xFF7E3F24.toInt(), true)
    drawTextLine(canvas, item.name, 122f, 255f, 74f, 0xFF2D2018.toInt(), true)
    drawTextLine(canvas, item.type.uppercase(), 122f, 318f, 28f, 0xFF0F7A78.toInt(), true)

    val potLeft = width * 0.30f
    val potTop = 400f
    canvas.drawOval(RectF(potLeft, potTop, width * 0.70f, 760f), clayPaint)
    canvas.drawOval(RectF(width * 0.37f, potTop + 18f, width * 0.63f, potTop + 92f), lightPaint)
    canvas.drawOval(RectF(width * 0.41f, potTop + 44f, width * 0.59f, potTop + 78f), darkPaint)
    canvas.drawArc(RectF(width * 0.34f, 560f, width * 0.66f, 700f), 18f, 144f, false, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x88FFFFFF.toInt()
        strokeWidth = 12f
        style = Paint.Style.STROKE
    })

    drawTextLine(canvas, "HEALTH BENEFIT", 122f, 850f, 30f, 0xFF0F7A78.toInt(), true)
    drawWrappedText(canvas, item.benefit, 122f, 905f, width - 244f, 44f, 0xFF38251A.toInt(), true)
    drawTextLine(canvas, "ECO STORY", 122f, 1060f, 30f, 0xFF7E3F24.toInt(), true)
    drawWrappedText(canvas, item.ecoTip, 122f, 1115f, width - 244f, 36f, 0xFF6F5B4C.toInt(), false)
    drawTextLine(canvas, "By $artisanName", 122f, 1234f, 32f, 0xFF2D2018.toInt(), true)
    drawTextLine(canvas, "$village | WhatsApp: $phoneNumber", 122f, 1286f, 29f, 0xFF0F7A78.toInt(), true)

    val outputDir = File(context.cacheDir, "story_cards")
    outputDir.mkdirs()
    val outputFile = File(outputDir, "kumbara_kala_story_card.png")
    FileOutputStream(outputFile).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", outputFile)
}

private fun drawTextLine(canvas: AndroidCanvas, text: String, x: Float, y: Float, size: Float, color: Int, bold: Boolean) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        textSize = size
        typeface = if (bold) android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
    }
    canvas.drawText(text, x, y, paint)
}

private fun drawWrappedText(
    canvas: AndroidCanvas,
    text: String,
    x: Float,
    y: Float,
    maxWidth: Float,
    size: Float,
    color: Int,
    bold: Boolean
) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        textSize = size
        typeface = if (bold) android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
    }
    var line = ""
    var currentY = y
    text.split(" ").forEach { word ->
        val candidate = if (line.isEmpty()) word else "$line $word"
        if (paint.measureText(candidate) > maxWidth) {
            canvas.drawText(line, x, currentY, paint)
            currentY += size * 1.25f
            line = word
        } else {
            line = candidate
        }
    }
    if (line.isNotEmpty()) {
        canvas.drawText(line, x, currentY, paint)
    }
}

private fun Color.toArgbInt(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}
