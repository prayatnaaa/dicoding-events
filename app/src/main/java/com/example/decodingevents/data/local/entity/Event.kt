package com.example.decodingevents.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "event")
@Parcelize
data class Event(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "summary")
    var summary: String? = null,

    @ColumnInfo(name = "media_cover")
    val mediaCover: String,

    @ColumnInfo(name = "registrants")
    val registrants: Int = 0,

    @ColumnInfo(name = "image_logo")
    val imageLogo: String,

    @ColumnInfo(name = "link")
    val link: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "owner_name")
    var ownerName: String,

    @ColumnInfo(name = "city_name")
    var cityName: String,

    @ColumnInfo(name = "quota")
    var quota: Int = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "begin_time")
    var beginTime: String,

    @ColumnInfo(name = "end_time")
    var endTime: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "isFavourite")
    var isFavourite: Boolean,

    @ColumnInfo(name = "isActive")
    var isActive: Boolean
) : Parcelable
