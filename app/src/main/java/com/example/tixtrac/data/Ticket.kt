package com.example.tixtrac.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class Ticket(
    val firstName : String,
    val lastName : String,
    val ticketTier : String,
    val qrDrawable: Drawable,
    val checkedInStatus : Boolean = false
)
