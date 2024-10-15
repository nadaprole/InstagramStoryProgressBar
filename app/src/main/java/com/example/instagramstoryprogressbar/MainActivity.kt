package com.example.instagramstoryprogressbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainScreen()
            }
        }
    }
}

@Composable
fun StoryProgressBar(
    stories: List<Story>,
    currentStoryIndex: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        stories.forEachIndexed { index, story ->
            val progress = remember { Animatable(0f) }

            if (index == currentStoryIndex) {
                LaunchedEffect(key1 = story.id) {
                    progress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = story.duration,
                            easing = LinearEasing
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .padding(horizontal = 2.dp)
                    .background(
                        androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.5f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.value)
                        .background(
                            androidx.compose.ui.graphics.Color.White,
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun InstagramStory(
    stories: List<Story>
) {
    var currentStoryIndex by remember { mutableStateOf(0) }

    if (currentStoryIndex < stories.size) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = stories[currentStoryIndex].image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            StoryProgressBar(stories, currentStoryIndex)
        }
        LaunchedEffect(currentStoryIndex) {
            delay(stories[currentStoryIndex].duration.toLong())
            currentStoryIndex += 1
        }
    } else {
        // Reset to the beginning to create a loop effect
        currentStoryIndex = 0
    }
}

@Composable
fun MainScreen() {
    val stories = listOf(
        Story(id = 1, duration = 3000, image = R.drawable.balcony),
        Story(id = 2, duration = 3000, image = R.drawable.bird),
        Story(id = 3, duration = 3000, image = R.drawable.poppies)
    )

    InstagramStory(stories)
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        MainScreen()
    }
}

data class Story(
    val id: Int,
    val duration: Int, // duration in milliseconds
    val image: Int
)