package com.example.tixtrac.data

import android.graphics.Bitmap

data class Ticket(
    val firstName : String,
    val lastName : String,
    val ticketTier : String,
    val qrBitmapString : String,
    val checkedInStatus : Boolean = false
)
