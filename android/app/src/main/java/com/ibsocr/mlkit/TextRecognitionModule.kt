package com.ibsocr.mlkit

import android.net.Uri
import com.facebook.react.bridge.Promise

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class TextRecognitionModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
      return "TextRecognitionModule";
    }


    @ReactMethod
    fun recognize_old(url: String, promise: Promise) {
        Log.d("TextRecognitionModule", "Found url: $url")

        val uri = Uri.parse(url)
        try {
            val image = InputImage.fromFilePath(reactApplicationContext, uri)

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { result ->
                    val blocksArray = Arguments.createArray()

                    for (block in result.textBlocks) {
                        val blockMap = Arguments.createMap()
                        blockMap.putString("text", block.text)

                        val blockCornerPointsArray = Arguments.createArray()
                        block.cornerPoints?.forEach { point ->
                            val pointMap = Arguments.createMap().apply {
                                putDouble("x", point.x.toDouble())
                                putDouble("y", point.y.toDouble())
                            }
                            blockCornerPointsArray.pushMap(pointMap)
                        }
                        blockMap.putArray("cornerPoints", blockCornerPointsArray)

                        val blockFrameMap = block.boundingBox?.let { rect ->
                            Arguments.createMap().apply {
                                putDouble("left", rect.left.toDouble())
                                putDouble("top", rect.top.toDouble())
                                putDouble("right", rect.right.toDouble())
                                putDouble("bottom", rect.bottom.toDouble())
                            }
                        }
                        blockMap.putMap("boundingBox", blockFrameMap)

                        val linesArray = Arguments.createArray()
                        for (line in block.lines) {
                            val lineMap = Arguments.createMap()
                            lineMap.putString("text", line.text)

                            val lineCornerPointsArray = Arguments.createArray()
                            line.cornerPoints?.forEach { point ->
                                val pointMap = Arguments.createMap().apply {
                                    putDouble("x", point.x.toDouble())
                                    putDouble("y", point.y.toDouble())
                                }
                                lineCornerPointsArray.pushMap(pointMap)
                            }
                            lineMap.putArray("cornerPoints", lineCornerPointsArray)

                            val lineFrameMap = line.boundingBox?.let { rect ->
                                Arguments.createMap().apply {
                                    putDouble("left", rect.left.toDouble())
                                    putDouble("top", rect.top.toDouble())
                                    putDouble("right", rect.right.toDouble())
                                    putDouble("bottom", rect.bottom.toDouble())
                                }
                            }
                            lineMap.putMap("boundingBox", lineFrameMap)

                            val elementsArray = Arguments.createArray()
                            for (element in line.elements) {
                                val elementMap = Arguments.createMap()
                                elementMap.putString("text", element.text)

                                val elementCornerPointsArray = Arguments.createArray()
                                element.cornerPoints?.forEach { point ->
                                    val pointMap = Arguments.createMap().apply {
                                        putDouble("x", point.x.toDouble())
                                        putDouble("y", point.y.toDouble())
                                    }
                                    elementCornerPointsArray.pushMap(pointMap)
                                }
                                elementMap.putArray("cornerPoints", elementCornerPointsArray)

                                val elementFrameMap = element.boundingBox?.let { rect ->
                                    Arguments.createMap().apply {
                                        putDouble("left", rect.left.toDouble())
                                        putDouble("top", rect.top.toDouble())
                                        putDouble("right", rect.right.toDouble())
                                        putDouble("bottom", rect.bottom.toDouble())
                                    }
                                }
                                elementMap.putMap("boundingBox", elementFrameMap)

                                elementsArray.pushMap(elementMap)
                            }
                            lineMap.putArray("elements", elementsArray)
                            linesArray.pushMap(lineMap)
                        }
                        blockMap.putArray("lines", linesArray)
                        blocksArray.pushMap(blockMap)
                    }

                    // Send result to JS
                    promise.resolve(
                        Arguments.createMap().apply {
                            putArray("blocks", blocksArray)
                        }
                    )
                }
                .addOnFailureListener { e ->
                    promise.reject("TextRecognitionError", e)
                }

        } catch (e: IOException) {
            Log.e("TextRecognitionModule", "Error in processing image", e)
            promise.reject("TextRecognitionError", e)
        }
    }

    @ReactMethod
fun recognize(url: String, promise: Promise) {
    Log.d("TextRecognitionModule", "Found url: $url")

    val uri = Uri.parse(url)
    try {
        val image = InputImage.fromFilePath(reactApplicationContext, uri)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                val blocksArray = Arguments.createArray()

                for (block in result.textBlocks) {
                    val blockMap = Arguments.createMap()
                    blockMap.putString("text", block.text)

                    val blockFrameMap = block.boundingBox?.let { rect ->
                        Arguments.createMap().apply {
                            putDouble("left", rect.left.toDouble())
                            putDouble("top", rect.top.toDouble())
                            putDouble("height", (rect.bottom - rect.top).toDouble())
                            putDouble("width", (rect.right - rect.left).toDouble())
                        }
                    }
                    blockMap.putMap("rect", blockFrameMap)

                    val linesArray = Arguments.createArray()
                    for (line in block.lines) {
                        val lineMap = Arguments.createMap()
                        lineMap.putString("text", line.text)

                        val lineFrameMap = line.boundingBox?.let { rect ->
                            Arguments.createMap().apply {
                                putDouble("left", rect.left.toDouble())
                                putDouble("top", rect.top.toDouble())
                                putDouble("height", (rect.bottom - rect.top).toDouble())
                                putDouble("width", (rect.right - rect.left).toDouble())
                            }
                        }
                        lineMap.putMap("rect", lineFrameMap)

                        val elementsArray = Arguments.createArray()
                        for (element in line.elements) {
                            val elementMap = Arguments.createMap()
                            elementMap.putString("text", element.text)

                            val elementFrameMap = element.boundingBox?.let { rect ->
                                Arguments.createMap().apply {
                                    putDouble("left", rect.left.toDouble())
                                    putDouble("top", rect.top.toDouble())
                                    putDouble("height", (rect.bottom - rect.top).toDouble())
                                    putDouble("width", (rect.right - rect.left).toDouble())
                                }
                            }
                            elementMap.putMap("rect", elementFrameMap)

                            elementsArray.pushMap(elementMap)
                        }
                        lineMap.putArray("elements", elementsArray)
                        linesArray.pushMap(lineMap)
                    }
                    blockMap.putArray("lines", linesArray)
                    blocksArray.pushMap(blockMap)
                }

                // Send result to JS
                promise.resolve(
                    Arguments.createMap().apply {
                        putDouble("width", image.width.toDouble()) // Adjust if necessary
                        putDouble("height", image.height.toDouble()) // Adjust if necessary
                        putArray("blocks", blocksArray)
                    }
                )
            }
            .addOnFailureListener { e ->
                promise.reject("TextRecognitionError", e)
            }

    } catch (e: IOException) {
        Log.e("TextRecognitionModule", "Error in processing image", e)
        promise.reject("TextRecognitionError", e)
    }
}




}