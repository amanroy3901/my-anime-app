package com.avfusion.apps.myanime.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avfusion.apps.myanime.data.model.AnimeEntity
import com.avfusion.apps.myanime.ui.theme.GoldenColor
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimeItemCard(
    anime: AnimeEntity, onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    onItemClick()
                }
        ) {
            GlideImage(model = anime.posterUrl,
                contentDescription = "Poster of ${anime.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop)

            Text(
                text = anime.title,
                fontSize = 16.sp,
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${anime.episodes} Episodes", fontSize = 12.sp
                )

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${anime.score}",
                        fontSize = 12.sp,
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        modifier = Modifier.size(16.dp),
                        tint = GoldenColor
                    )
                }
            }
        }
    }
}
