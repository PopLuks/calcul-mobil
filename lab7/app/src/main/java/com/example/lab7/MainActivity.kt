package com.example.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab7.ui.theme.Lab7Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab7Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImageGridScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ImageGridScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Lista de resurse drawable pentru cele 9 imagini
    // Imaginile trebuie să fie în res/drawable cu numele: img_0, img_1, ..., img_8
    // Suportă atât .png cât și .jpg
    val imageResources = remember {
        // Try to dynamically load images from drawable
        (0..8).map { index ->
            val resName = "img_$index"  // căutăm img_0.jpg, img_1.jpg, etc.
            val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)

            if (resId == 0) {
                // Try alternative naming: digit_0, digit_1, etc.
                val altName = "digit_$index"
                context.resources.getIdentifier(altName, "drawable", context.packageName)
            } else {
                resId
            }
        }
    }

    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading state with coroutines
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // Simulate loading delay
            kotlinx.coroutines.delay(500)
        }
        isLoading = false
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Digit Images Grid (3x3)",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(imageResources) { resourceId ->
                    ImageCard(resourceId = resourceId)
                }
            }
        }
    }
}

@Composable
fun ImageCard(resourceId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (resourceId != 0) {
                // Load image from drawable resources
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = "Image from drawable",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            } else {
                // Placeholder when image not found
                Text(
                    text = "No Image",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageGridPreview() {
    Lab7Theme {
        ImageGridScreen()
    }
}

