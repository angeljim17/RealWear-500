package com.example.skillfactory
import android.media.MediaPlayer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var animacionFinal: LottieAnimationView

    private var currentVideoIndex = 0

    private val videoList = listOf(
        R.raw.introduccion,
        R.raw.paso1,
        R.raw.paso2,
        R.raw.paso3,
        R.raw.paso4,
        R.raw.paso5,
        R.raw.paso6
    )

    // BroadcastReceiver para RealWear HF clásico
    private val speechReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val command = intent?.getStringExtra("command")
            Log.d("SkillFactory", "Comando recibido (HF): $command")

            when (command?.trim()?.lowercase()) {
                "siguiente video", "siguiente_video" -> avanzarVideo()
                "repetir video", "repetir_video" -> reiniciarVideo()
                else -> {
                    Toast.makeText(
                        this@MainActivity,
                        "Comando no reconocido: $command",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("SkillFactory", "Comando NO reconocido: $command")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reproductor)

        Toast.makeText(this, "Di 'Siguiente video' para comenzar", Toast.LENGTH_LONG).show()

        playerView = findViewById(R.id.player_view)
        animacionFinal = findViewById(R.id.animacionFinal)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // Listener para detectar fin de video
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    if (currentVideoIndex == videoList.size) {
                        mostrarAnimacionFinal()
                    }
                }
            }
        })

        // Registrar receptor de voz para RealWear HF
        val filter = IntentFilter("com.realwear.wearhf.intent.action.SPEECH_EVENT")
        registerReceiver(speechReceiver, filter, RECEIVER_EXPORTED)

        Log.d("SkillFactory", "Aplicación iniciada, esperando comandos de voz")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("SkillFactory", "Intent recibido por WearML: ${intent?.action}")

        when (intent?.action) {
            "com.realwear.skillfactory.SIGUIENTE_VIDEO" -> avanzarVideo()
            "com.realwear.skillfactory.REPETIR_VIDEO" -> reiniciarVideo()
        }
    }

    private fun avanzarVideo() {
        if (currentVideoIndex < videoList.size) {
            val videoResId = videoList[currentVideoIndex]
            val uri = Uri.parse("android.resource://${packageName}/$videoResId")
            val mediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
            Log.d("SkillFactory", "Reproduciendo video index: $currentVideoIndex")
            currentVideoIndex++
        } else {
            Toast.makeText(this, "No hay más videos.", Toast.LENGTH_SHORT).show()
            Log.d("SkillFactory", "Último video alcanzado.")
        }
    }

    private fun reiniciarVideo() {
        if (currentVideoIndex == 0) {
            Toast.makeText(this, "No hay video actual para repetir.", Toast.LENGTH_SHORT).show()
            return
        }

        val reiniciarIndex = currentVideoIndex - 1
        val videoResId = videoList[reiniciarIndex]
        val uri = Uri.parse("android.resource://${packageName}/$videoResId")
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        Log.d("SkillFactory", "Reiniciando video index: $reiniciarIndex")
    }

    private fun mostrarAnimacionFinal() {
        runOnUiThread {
            playerView.visibility = android.view.View.GONE
            animacionFinal.visibility = android.view.View.VISIBLE
            animacionFinal.playAnimation()

            val mediaPlayer= MediaPlayer.create(this,R.raw.final_sound)
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(speechReceiver)
        player.release()
    }
}






