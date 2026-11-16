package com.theveloper.pixelplay.ui.glancewidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import com.theveloper.pixelplay.MainActivity
import com.theveloper.pixelplay.data.model.PlayerInfo
import kotlin.text.ifEmpty

class BarWidget4x1 : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact
    override val stateDefinition = PlayerInfoStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val playerInfo = currentState<PlayerInfo>()
            GlanceTheme {
                BarWidget4x1Content(playerInfo = playerInfo, context = context)
            }
        }
    }

    @Composable
    private fun BarWidget4x1Content(
        playerInfo: PlayerInfo,
        context: Context
    ) {
        val title = playerInfo.songTitle.ifEmpty { "PixelPlayer" }
        val artist = playerInfo.artistName.ifEmpty { "Tap to open" }
        val isPlaying = playerInfo.isPlaying
        val albumArtBitmapData = playerInfo.albumArtBitmapData
        val albumArtUri = playerInfo.albumArtUri

        val colors = playerInfo.getWidgetColors()

        // Dynamic border color extracted from album art or primary accent
        val borderColor = colors.playPauseBackground
        val transparentColor = ColorProvider(day = Color.Transparent, night = Color.Transparent)
        val whiteIconColor = ColorProvider(day = Color.White, night = Color.White)
        val blackBgColor = ColorProvider(day = Color.Black, night = Color.Black)
        val softGreyColor = ColorProvider(day = Color(0xFFCCCCCC), night = Color(0xFFCCCCCC))

        val pillCornerRadius = 100.dp
        val rotationAngle = if (isPlaying) ((playerInfo.currentPositionMs / 40L) % 360L).toFloat() else 0f

        // Outer container: Dynamic glowing border ring
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(borderColor)
                .cornerRadius(pillCornerRadius)
                .padding(2.5.dp)
        ) {
            // Inner container: Dark stadium pill background
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(blackBgColor)
                    .cornerRadius(pillCornerRadius)
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .clickable(actionStartActivity<MainActivity>())
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.Horizontal.Start
                ) {
                    // Integrated Circular Cover Art on left edge (spinning when playing)
                    AlbumArtImage(
                        modifier = GlanceModifier.size(54.dp),
                        bitmapData = albumArtBitmapData,
                        albumArtUri = albumArtUri,
                        size = 54.dp,
                        context = context,
                        cornerRadius = 100.dp,
                        isCircular = true,
                        rotationAngle = rotationAngle
                    )

                    Spacer(GlanceModifier.width(12.dp))

                    // Center Typography: White title + Soft light-grey artist
                    Column(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = whiteIconColor
                            ),
                            maxLines = 1
                        )
                        Spacer(GlanceModifier.height(2.dp))
                        Text(
                            text = artist,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = softGreyColor
                            ),
                            maxLines = 1
                        )
                    }

                    Spacer(GlanceModifier.width(8.dp))

                    // Minimalist Flat Playback Controls (Right End)
                    // Previous (Rewind <<)
                    PreviousButton(
                        modifier = GlanceModifier.size(36.dp),
                        backgroundColor = transparentColor,
                        iconColor = whiteIconColor,
                        cornerRadius = 0.dp,
                        iconSize = 22.dp
                    )

                    Spacer(GlanceModifier.width(4.dp))

                    // Play / Pause (Center ▶ / ❚❚)
                    PlayPauseButton(
                        modifier = GlanceModifier.size(40.dp),
                        isPlaying = isPlaying,
                        backgroundColor = transparentColor,
                        iconColor = whiteIconColor,
                        cornerRadius = 0.dp,
                        iconSize = 26.dp
                    )

                    Spacer(GlanceModifier.width(4.dp))

                    // Next (Fast-Forward >>)
                    NextButton(
                        modifier = GlanceModifier.size(36.dp),
                        backgroundColor = transparentColor,
                        iconColor = whiteIconColor,
                        cornerRadius = 0.dp,
                        iconSize = 22.dp
                    )

                    Spacer(GlanceModifier.width(6.dp))
                }
            }
        }
    }
}
