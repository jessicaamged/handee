package com.example.handee

import android.content.Intent
import com.example.handee.unity.HandeeUnityRegistrar
import com.example.handee.unity.HandeeUnitySignActivity
import com.example.handee.unity.HandeeUnityUtils
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "ai_channel"
    private val UNITY_CHANNEL = "handee_unity"

    companion object {
        private const val UNITY_OBJECT = "AvatarController"
        private const val UNITY_METHOD = "PlaySign"
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        HandeeUnityRegistrar.register(flutterEngine, this)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, UNITY_CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "postMessage" -> {
                        val gameObject =
                            call.argument<String>("gameObject") ?: UNITY_OBJECT
                        val methodName =
                            call.argument<String>("methodName") ?: UNITY_METHOD
                        val message = call.argument<String>("message") ?: ""
                        HandeeUnityUtils.prepareForMessage()
                        HandeeUnityUtils.postMessage(gameObject, methodName, message)
                        result.success(true)
                    }
                    "prepareUnity" -> {
                        HandeeUnityUtils.prepareForMessage()
                        result.success(true)
                    }
                    "openSign" -> {
                        val word = call.argument<String>("word")?.trim().orEmpty()
                        if (word.isEmpty()) {
                            result.success(false)
                            return@setMethodCallHandler
                        }
                        try {
                            val intent = Intent(this, HandeeUnitySignActivity::class.java)
                            intent.putExtra(HandeeUnitySignActivity.EXTRA_WORD, word)
                            startActivity(intent)
                            result.success(true)
                        } catch (e: Exception) {
                            result.success(false)
                        }
                    }
                    "isReady" -> {
                        result.success(
                            HandeeUnityUtils.unityLoaded && HandeeUnityUtils.sceneReady,
                        )
                    }
                    else -> result.notImplemented()
                }
            }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "getPrediction" -> result.success("AI is working 🔥")
                    "openCamera" -> {
                        startActivity(Intent(this, CameraActivity::class.java))
                        result.success("opened")
                    }
                    else -> result.notImplemented()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        HandeeUnityUtils.activity = this
        HandeeUnityUtils.resume()
        HandeeUnityUtils.focus()
    }

    override fun onStop() {
        HandeeUnityUtils.pause()
        super.onStop()
    }
}
