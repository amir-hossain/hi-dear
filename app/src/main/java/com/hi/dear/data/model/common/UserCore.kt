package com.hi.dear.data.model.common

import android.os.Parcel
import android.os.Parcelable

open class UserCore(
    var id: String? = null,
    var name: String? = null,
    var picture: String? = null,
    var gender: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(picture)
        parcel.writeString(gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserCore> {
        override fun createFromParcel(parcel: Parcel): UserCore {
            return UserCore(parcel)
        }

        override fun newArray(size: Int): Array<UserCore?> {
            return arrayOfNulls(size)
        }
    }
}