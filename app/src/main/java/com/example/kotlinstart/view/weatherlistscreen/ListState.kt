package com.example.kotlinstart.view.weatherlistscreen

import android.os.Parcel
import android.os.Parcelable

internal sealed class ListState: Parcelable {

    data class NotChanged(val refresh: Boolean) : ListState() {
        constructor(parcel: Parcel) : this(parcel.readByte() != 0.toByte()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (refresh) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<NotChanged> {
            override fun createFromParcel(parcel: Parcel): NotChanged {
                return NotChanged(parcel)
            }

            override fun newArray(size: Int): Array<NotChanged?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class ToPosition(val position: Int, val refresh: Boolean) : ListState() {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readByte() != 0.toByte()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(position)
            parcel.writeByte(if (refresh) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ToPosition> {
            override fun createFromParcel(parcel: Parcel): ToPosition {
                return ToPosition(parcel)
            }

            override fun newArray(size: Int): Array<ToPosition?> {
                return arrayOfNulls(size)
            }
        }
    }
}
