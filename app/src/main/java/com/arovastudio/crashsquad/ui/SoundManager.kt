package com.arovastudio.crashsquad.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context, private val scope: CoroutineScope) {

    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    var soundEnabled: Boolean = true
    var musicEnabled: Boolean = true
    var vibrationEnabled: Boolean = true

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error init ToneGenerator: ${e.message}")
        }
    }

    fun playSound(name: String) {
        if (!soundEnabled) return
        scope.launch(Dispatchers.Default) {
            try {
                when (name) {
                    "click", "tap" -> playTone(ToneGenerator.TONE_PROP_BEEP, 35)
                    "slingshot_pull" -> playSynthesizedTone(300f, 480f, 60)
                    "launch" -> playSynthesizedTone(600f, 220f, 80)
                    "hit_thud" -> playSynthesizedTone(180f, 90f, 70)
                    "wood_crack" -> playSynthesizedTone(440f, 150f, 90)
                    "stone_crack" -> playSynthesizedTone(280f, 70f, 120)
                    "explosion" -> playSynthesizedNoise(200)
                    "enemy_pop" -> playSynthesizedTone(523f, 880f, 110)
                    "gem_pickup", "coin" -> playSynthesizedTone(880f, 1320f, 140)
                    "victory" -> playFanfare()
                    "defeat" -> playDefeatSound()
                }
            } catch (e: Exception) {
                Log.w("SoundManager", "Audio playback issue: ${e.message}")
            }
        }
    }

    fun vibrate(durationMs: Long = 40, amplitude: Int = 180) {
        if (!vibrationEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (e: Exception) {
        }
    }

    private fun playTone(toneType: Int, durationMs: Int) {
        toneGenerator?.startTone(toneType, durationMs)
    }

    private fun playSynthesizedTone(startFreq: Float, endFreq: Float, durationMs: Int) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
        val generatedSnd = ByteArray(2 * numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toFloat() / numSamples
            val currentFreq = startFreq + (endFreq - startFreq) * progress
            val angle = 2.0 * Math.PI * i / (sampleRate / currentFreq)
            val envelope = (1.0 - progress)
            val sample = (sin(angle) * 32767.0 * envelope).toInt().toShort()

            generatedSnd[2 * i] = (sample.toInt() and 0x00ff).toByte()
            generatedSnd[2 * i + 1] = ((sample.toInt() and 0xff00) ushr 8).toByte()
        }

        playAudioTrack(generatedSnd, sampleRate)
    }

    private fun playSynthesizedNoise(durationMs: Int) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
        val generatedSnd = ByteArray(2 * numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toFloat() / numSamples
            val envelope = (1.0 - progress)
            val sample = ((Math.random() * 2.0 - 1.0) * 32767.0 * envelope).toInt().toShort()

            generatedSnd[2 * i] = (sample.toInt() and 0x00ff).toByte()
            generatedSnd[2 * i + 1] = ((sample.toInt() and 0xff00) ushr 8).toByte()
        }

        playAudioTrack(generatedSnd, sampleRate)
    }

    private fun playFanfare() {
        playSynthesizedTone(523f, 659f, 90)
        Thread.sleep(100)
        playSynthesizedTone(659f, 784f, 90)
        Thread.sleep(100)
        playSynthesizedTone(784f, 1046f, 220)
    }

    private fun playDefeatSound() {
        playSynthesizedTone(440f, 330f, 140)
        Thread.sleep(130)
        playSynthesizedTone(330f, 220f, 220)
    }

    private fun playAudioTrack(pcmData: ByteArray, sampleRate: Int) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcmData.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(pcmData, 0, pcmData.size)
            audioTrack.play()
            scope.launch(Dispatchers.Default) {
                Thread.sleep(500)
                audioTrack.release()
            }
        } catch (e: Exception) {
        }
    }
}
