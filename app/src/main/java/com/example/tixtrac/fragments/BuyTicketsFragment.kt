package com.example.tixtrac.fragments

import android.R.attr.bitmap
import android.R.attr.visible
import android.graphics.Bitmap
import android.graphics.Rect
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.os.IResultReceiver2.Default
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tixtrac.R
import com.example.tixtrac.data.Ticket
import com.example.tixtrac.databinding.FragmentBuyTicketsBinding
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
            makeTicketQR(firstName, lastName, ticketTier)
        }
    }

    private fun makeTicketQR(firstName: String, lastName: String, ticketTier: String) {
        val color = Color()
        color.light = 0xFFFFFFFF.toInt()
        color.dark = 0xFF000000.toInt()
        color.background = 0xFFFFFFFF.toInt()
        color.auto = false

        val renderOption = RenderOption()
        renderOption.content = "$firstName\n$lastName\n$ticketTier"
        renderOption.size = 800
        renderOption.borderWidth = 20
        renderOption.patternScale = 0.35f
        renderOption.roundedPatterns = false
        renderOption.clearBorder = true
        renderOption.color = color

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = AwesomeQrRenderer.render(renderOption)
                if (result.bitmap != null) {
                    withContext(Dispatchers.Main){
                        binding.ivTicketQR.setImageBitmap(result.bitmap)
                    }
                    addToDatabase(firstName, lastName, ticketTier, result.bitmap.toString())
                }
                else {
                    Toast.makeText(
                        requireContext(),
                        "QR Bitmap is null",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun addToDatabase(firstName: String, lastName: String, ticketTier: String, qrBitmapString: String){
        val ticket = Ticket(firstName, lastName, ticketTier, qrBitmapString)
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