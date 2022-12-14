package com.example.notesappfragment.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Notes(


    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "subtitle")
    val subtitle: String = "",
    @ColumnInfo(name = "datetime")
    val datetime: String = "",

    @ColumnInfo(name = "note_Text")
    val noteText: String = "",

    @ColumnInfo(name = "image_path")
    var imagePath: String = "",

    @ColumnInfo(name = "color")
    var color: String = "",
    @ColumnInfo(name = "web_link")
    var weblink: String = "",
    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),

        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(datetime)
        parcel.writeString(noteText)
        parcel.writeString(imagePath)
        parcel.writeString(color)
        parcel.writeString(weblink)
        parcel.writeByte(if (favorite) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notes> {
        override fun createFromParcel(parcel: Parcel): Notes {
            return Notes(parcel)
        }

        override fun newArray(size: Int): Array<Notes?> {
            return arrayOfNulls(size)
        }
    }
}





