package com.example.notesappfragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Convertor {

@TypeConverter
    fun fromBitMap(bitmap: Bitmap):ByteArray{
        val outputStream =ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
        return outputStream.toByteArray()
    }
@TypeConverter
    fun toBitMap(byteArray: ByteArray):Bitmap
    {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

}