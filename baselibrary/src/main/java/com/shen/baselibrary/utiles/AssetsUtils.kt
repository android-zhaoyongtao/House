package com.shen.baselibrary.utiles

import android.content.Context
import com.google.gson.stream.JsonReader
import com.shen.baselibrary.http.Convert
import java.io.InputStreamReader
import java.lang.reflect.Type

object AssetsUtils {
    fun <T> getObjectFromAssets(context: Context, assetsName: String, classOfT: Class<T>): T? {
        try {
            val inputStream = context.assets.open(assetsName)
            val reader = InputStreamReader(inputStream, "utf-8")
            val jsonReader = JsonReader(reader)
            val t = Convert.fromJson<T>(jsonReader, classOfT)
            reader.close()
            inputStream.close()
            return t
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun <T> getObjectFromAssets(context: Context, assetsName: String, typeOfT: Type): T? {
        try {
            val inputStream = context.assets.open(assetsName)
            val reader = InputStreamReader(inputStream, "utf-8")
            val jsonReader = JsonReader(reader)
            val t = Convert.fromJson<T>(jsonReader, typeOfT)
            reader.close()
            inputStream.close()
            return t
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}