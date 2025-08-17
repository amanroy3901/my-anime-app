package com.avfusion.apps.myanime.ui.screens.animedetails

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity
import com.avfusion.apps.myanime.data.model.Genre
import com.avfusion.apps.myanime.data.model.VoiceActor
import com.avfusion.apps.myanime.ui.Utils.AnimeDetailsUiState
import com.avfusion.apps.myanime.ui.screens.NoDataAndNoNetworkScreen
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimeDetailsScreen(
    viewModel: AnimeDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showVoiceActorImage = viewModel.showVoiceActorImage

    when (uiState) {
        is AnimeDetailsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AnimeDetailsUiState.Success -> {
            val details = (uiState as AnimeDetailsUiState.Success).animeDetails
            val characters = (uiState as AnimeDetailsUiState.Success).characters

            AnimeDetailsContent(
                animeDetails = details,
                animeCharacters = characters,
                showVoiceActorImage = showVoiceActorImage,
                onToggleVoiceActorImage = viewModel::toggleVoiceActorImage
            )
        }
        is AnimeDetailsUiState.ErrorNoDataAndNoNetwork -> {
            NoDataAndNoNetworkScreen(onRetry = { viewModel.fetchData() })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimeDetailsContent(
    animeDetails: AnimeDetailsEntity,
    animeCharacters: List<CharacterEntity>,
    showVoiceActorImage: Boolean,
    onToggleVoiceActorImage: () -> Unit
) {
    var showPlayer by remember { mutableStateOf(false) }

    val trailerId = animeDetails.trailer?.youtubeId
    val thumbnailUrl = animeDetails.trailer?.maximumImageUrl
    val posterUrl = animeDetails.posterUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        ) {
            if (trailerId != null && showPlayer) {
                YouTubePlayer(
                    videoId = trailerId, modifier = Modifier.fillMaxSize()
                )
            } else if (trailerId != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showPlayer = true },
                    contentAlignment = Alignment.Center
                ) {
                    GlideImage(
                        model = thumbnailUrl,
                        contentDescription = "Anime Trailer Thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play Video",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            } else if (posterUrl != null) {
                GlideImage(
                    model = posterUrl,
                    contentDescription = "Anime Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = animeDetails.title ?: "No Title Found",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Score: ${animeDetails.score?.toString() ?: "N/A"}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = "Episodes: ${animeDetails.episodes?.toString() ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            animeDetails.genres?.let { genresList ->
                if (genresList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(genresList) { genre ->
                            GenreChip(genre = genre)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExpandableText(
                text = animeDetails.synopsis ?: "No synopsis available."
            )

            if (animeCharacters.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                CastSection(
                    characters = animeCharacters,
                    showVoiceActorImage = showVoiceActorImage,
                    onToggle = onToggleVoiceActorImage
                )
            }
        }
    }
}

@Composable
fun ExpandableText(text: String) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        if (expanded || text.length > 250) {
            Text(text = if (expanded) "Show Less" else "Show More",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(top = 4.dp))
        }
    }
}

@Composable
fun GenreChip(genre: Genre) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CastSection(
    characters: List<CharacterEntity>, showVoiceActorImage: Boolean, onToggle: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Main Cast",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Voice Actor Image",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Switch(checked = showVoiceActorImage, onCheckedChange = { onToggle() })
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(characters) { character ->
                CastMemberItem(
                    character = character, showVoiceActorImage = showVoiceActorImage
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CastMemberItem(
    character: CharacterEntity, showVoiceActorImage: Boolean
) {
    val characterImageUrl = character.images.jpg.imageUrl
    val voiceActor = character.voiceActors.find { it.language == "Japanese" }
    val voiceActorImageUrl = voiceActor?.person?.images?.jpg?.imageUrl
    val voiceActorName = voiceActor?.person?.name ?: "N/A"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.surface.copy(0.95f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = characterImageUrl,
            contentDescription = "Character image for ${character.name}",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = character.name.split(",").reversed().joinToString(" "),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = character.role,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = voiceActorName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = voiceActor?.language ?: "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (showVoiceActorImage && !voiceActorImageUrl.isNullOrEmpty()) {
            GlideImage(
                model = voiceActorImageUrl,
                contentDescription = "Voice actor image for $voiceActorName",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun YouTubePlayer(
    videoId: String, modifier: Modifier = Modifier
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                this.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }, modifier = modifier
    )
}