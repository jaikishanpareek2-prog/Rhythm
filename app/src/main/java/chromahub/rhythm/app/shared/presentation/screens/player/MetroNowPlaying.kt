/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package chromahub.rhythm.app.shared.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chromahub.rhythm.app.shared.presentation.components.icons.Icon
import chromahub.rhythm.app.shared.presentation.components.icons.RhythmIcons
import coil.compose.AsyncImage
import coil.request.ImageRequest
import chromahub.rhythm.app.shared.data.model.Song

/**
 * Compose native recreations of Metro's advertised Now Playing compositions.
 * The playback engine and Rhythm callbacks remain untouched.
 */
@Composable
fun MetroNowPlayingScreen(
    style: String,
    song: Song?,
    isPlaying: Boolean,
    progress: () -> Float,
    currentTimeStr: String,
    totalTimeStr: String,
    queuePosition: Int,
    queueTotal: Int,
    isShuffleEnabled: Boolean,
    repeatMode: Int,
    isFavorite: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onQueueClick: () -> Unit,
    onLocationClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val normalized = style.uppercase()
    val current = progress().coerceIn(0f, 1f)

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (normalized) {
            "FIT" -> FitLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "FLAT" -> FlatLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "COLOR" -> ColorLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "MATERIAL" -> MaterialLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "CLASSIC" -> ClassicLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "ADAPTIVE" -> AdaptiveLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "BLUR" -> BlurLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "TINY" -> TinyLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            "PEEK" -> PeekLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
            else -> NormalLayout(song, isPlaying, current, currentTimeStr, totalTimeStr, isFavorite, onPlayPause, onSeek, onSkipPrevious, onSkipNext, onToggleFavorite, onQueueClick, onBack)
        }

        // Keep the extra playback controls available on every Metro composition.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onToggleShuffle) {
                Icon(RhythmIcons.Player.Shuffle, contentDescription = "Shuffle", tint = if (isShuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            }
            IconButton(onClick = onToggleRepeat) {
                Icon(RhythmIcons.Player.Repeat, contentDescription = "Repeat")
            }
            IconButton(onClick = onLocationClick) {
                Icon(RhythmIcons.Actions.More, contentDescription = "More")
            }
        }
    }
}

@Composable
private fun NormalLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    PlayerColumn(song, playing, progress, current, total, favorite, playPause, seek, previous, next, favoriteClick, queue, back, 0.72f, RoundedCornerShape(30.dp))
}

@Composable
private fun FitLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(back, queue)
        Spacer(Modifier.height(8.dp))
        Artwork(song, Modifier.fillMaxWidth().aspectRatio(1f), RoundedCornerShape(18.dp))
        Spacer(Modifier.height(14.dp))
        SongInfo(song, favorite, favoriteClick)
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next)
    }
}

@Composable
private fun FlatLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.Center) {
        Header(back, queue)
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Artwork(song, Modifier.size(148.dp), RoundedCornerShape(18.dp))
            Spacer(Modifier.width(18.dp))
            SongInfo(song, favorite, favoriteClick, Modifier.weight(1f))
        }
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next)
    }
}

@Composable
private fun ColorLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    val container = MaterialTheme.colorScheme.primaryContainer
    Surface(Modifier.fillMaxSize(), color = container) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Header(back, queue)
            Spacer(Modifier.height(18.dp))
            Artwork(song, Modifier.fillMaxWidth(0.82f).aspectRatio(1f), RoundedCornerShape(34.dp))
            Spacer(Modifier.height(18.dp))
            SongInfo(song, favorite, favoriteClick)
            SeekBar(progress, seek, current, total)
            Controls(playing, playPause, previous, next)
        }
    }
}

@Composable
private fun MaterialLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(back, queue)
        Spacer(Modifier.height(22.dp))
        Surface(
            Modifier.fillMaxWidth().aspectRatio(1f),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp
        ) { Artwork(song, Modifier.fillMaxSize(), RoundedCornerShape(28.dp)) }
        Spacer(Modifier.height(18.dp))
        SongInfo(song, favorite, favoriteClick)
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next)
    }
}

@Composable
private fun ClassicLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(back, queue)
        Spacer(Modifier.height(22.dp))
        Artwork(song, Modifier.size(250.dp), CircleShape)
        Spacer(Modifier.height(18.dp))
        SongInfo(song, favorite, favoriteClick)
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next, classic = true)
    }
}

@Composable
private fun AdaptiveLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    androidx.compose.foundation.layout.BoxWithConstraints(Modifier.fillMaxSize()) {
        if (maxWidth > 650.dp) {
            Row(Modifier.fillMaxSize().padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Artwork(song, Modifier.weight(1f).aspectRatio(1f), RoundedCornerShape(28.dp))
                Spacer(Modifier.width(28.dp))
                Column(Modifier.weight(1f)) {
                    Header(back, queue)
                    SongInfo(song, favorite, favoriteClick)
                    SeekBar(progress, seek, current, total)
                    Controls(playing, playPause, previous, next)
                }
            }
        } else {
            NormalLayout(song, playing, progress, current, total, favorite, playPause, seek, previous, next, favoriteClick, queue, back)
        }
    }
}

@Composable
private fun BlurLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Artwork(song, Modifier.fillMaxSize().blur(70.dp).alpha(0.45f), RoundedCornerShape(0.dp))
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Header(back, queue)
                Spacer(Modifier.height(18.dp))
                Artwork(song, Modifier.fillMaxWidth(0.78f).aspectRatio(1f), RoundedCornerShape(28.dp))
                SongInfo(song, favorite, favoriteClick)
                SeekBar(progress, seek, current, total)
                Controls(playing, playPause, previous, next)
            }
        }
    }
}

@Composable
private fun TinyLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.Center) {
        Header(back, queue)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Artwork(song, Modifier.size(88.dp), RoundedCornerShape(14.dp))
            Spacer(Modifier.width(14.dp))
            SongInfo(song, favorite, favoriteClick, Modifier.weight(1f))
        }
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next)
    }
}

@Composable
private fun PeekLayout(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Artwork(song, Modifier.fillMaxWidth().aspectRatio(1f), RoundedCornerShape(0.dp))
        Surface(
            Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            tonalElevation = 8.dp
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Header(back, queue)
                SongInfo(song, favorite, favoriteClick)
                SeekBar(progress, seek, current, total)
                Controls(playing, playPause, previous, next)
            }
        }
    }
}

@Composable
private fun PlayerColumn(
    song: Song?, playing: Boolean, progress: Float, current: String, total: String, favorite: Boolean,
    playPause: () -> Unit, seek: (Float) -> Unit, previous: () -> Unit, next: () -> Unit,
    favoriteClick: () -> Unit, queue: () -> Unit, back: () -> Unit,
    artworkFraction: Float, shape: RoundedCornerShape
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(back, queue)
        Spacer(Modifier.height(8.dp))
        Artwork(song, Modifier.fillMaxWidth(artworkFraction).aspectRatio(1f), shape)
        Spacer(Modifier.height(16.dp))
        SongInfo(song, favorite, favoriteClick)
        SeekBar(progress, seek, current, total)
        Controls(playing, playPause, previous, next)
    }
}

@Composable
private fun Header(back: () -> Unit, queue: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = back) { Icon(RhythmIcons.Navigation.Back, "Back") }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = queue) { Icon(RhythmIcons.Player.Queue, "Queue") }
    }
}

@Composable
private fun Artwork(song: Song?, modifier: Modifier, shape: androidx.compose.ui.graphics.Shape) {
    Box(modifier.clip(shape).background(MaterialTheme.colorScheme.surfaceContainer)) {
        if (song?.artworkUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(song.artworkUri).crossfade(true).build(),
                contentDescription = song.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = "♪",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun SongInfo(song: Song?, favorite: Boolean, favoriteClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(song?.title ?: "Nothing playing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 2)
            IconButton(onClick = favoriteClick) {
                Icon(if (favorite) RhythmIcons.Actions.Favorite else RhythmIcons.Actions.FavoriteOutlined, "Favorite")
            }
        }
        Text(song?.artist ?: "Unknown artist", style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        Text(song?.album ?: "", style = MaterialTheme.typography.bodySmall, maxLines = 1, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SeekBar(progress: Float, seek: (Float) -> Unit, current: String, total: String) {
    Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Slider(value = progress, onValueChange = seek)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(current, style = MaterialTheme.typography.labelSmall)
            Text(total, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun Controls(playing: Boolean, playPause: () -> Unit, previous: () -> Unit, next: () -> Unit, classic: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = previous) { Icon(RhythmIcons.Player.SkipPrevious, "Previous") }
        Surface(
            shape = if (classic) CircleShape else RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (classic) 64.dp else 58.dp).clickable(onClick = playPause)
        ) {
            Icon(
                if (playing) RhythmIcons.Player.Pause else RhythmIcons.Player.Play,
                contentDescription = if (playing) "Pause" else "Play",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(17.dp)
            )
        }
        IconButton(onClick = next) { Icon(RhythmIcons.Player.SkipNext, "Next") }
    }
}
