package com.example.tixtrac.fragments

import android.R.attr.bitmap
import android.R.attr.visible
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.os.IResultReceiver2.Default
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tixtrac.R
import com.example.tixtrac.data.Ticket
import com.example.tixtrac.databinding.FragmentBuyTicketsBinding
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.style.Color
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBackground
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColors
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogo
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoPadding
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorShapes
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.RenderResult
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.background.BlendBackground
import com.github.sumimakito.awesomeqr.option.background.StillBackground
import com.github.sumimakito.awesomeqr.option.color.Color
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "TixTrac Tag"

class BuyTicketsFragment : Fragment(R.layout.fragment_buy_tickets) {

    private lateinit var binding: FragmentBuyTicketsBinding
    private lateinit var databaseReference: CollectionReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyTicketsBinding.bind(view)
        databaseReference = Firebase.firestore.collection("ticket")

        binding.btnRSVP.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val ticketTier = binding.etTicketTier.text.toString()
            val qrDrawable = makeTicketQR(firstName, lastName, ticketTier)
            addToDatabase(firstName, lastName, ticketTier, qrDrawable)
        }
    }

    private fun makeTicketQR(firstName: String, lastName: String, ticketTier: String) : Drawable {
        val data = QrData.Text("$firstName\n$lastName\n$ticketTier")
        val drawable = QrCodeDrawable(data)
        binding.ivTicketQR.setImageDrawable(drawable)
        return drawable
    }

    private fun addToDatabase(firstName: String, lastName: String, ticketTier: String, qrDrawable: Drawable){
        val ticket = Ticket(firstName, lastName, ticketTier, qrDrawable)
        databaseReference.add(ticket)
            .addOnSuccessListener{
                Log.d(TAG, "Added document to DB")
            }
            .addOnFailureListener{
                Log.d(TAG, "Error adding document to DB")
            }
        Toast.makeText(
            requireContext(),
            "Ticket Bought",
            Toast.LENGTH_SHORT
        ).show()
    }
}