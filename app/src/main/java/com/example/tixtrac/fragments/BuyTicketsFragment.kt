package com.example.tixtrac.fragments

import android.R.attr.bitmap
import android.R.attr.visible
import android.graphics.Bitmap
import android.graphics.Rect
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tixtrac.R
import com.example.tixtrac.databinding.FragmentBuyTicketsBinding
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.RenderResult
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.background.BlendBackground
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "TixTrac Tag"

class BuyTicketsFragment : Fragment(R.layout.fragment_buy_tickets) {

    private lateinit var binding: FragmentBuyTicketsBinding
    private lateinit var firestoreRefrence: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyTicketsBinding.bind(view)
        firestoreRefrence = Firebase.firestore
        val db = firestoreRefrence.collection("ticket")

        binding.btnRSVP.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val ticketTier = binding.etTicketTier.text.toString()
            createQRTicket(firstName, lastName, ticketTier)
        }
    }

    private fun createQRTicket(firstName: String, lastName: String, ticketTier: String) {
        val renderOption = RenderOption()
        renderOption.content = "$firstName\n$lastName\n$ticketTier"
        renderOption.size = 800
        renderOption.borderWidth = 20
        renderOption.patternScale = 0.35f
        renderOption.roundedPatterns = true
        renderOption.clearBorder = true
        try {
            val result = AwesomeQrRenderer.render(renderOption)
            if (result.bitmap != null) {
                binding.ivTicketQR.setImageBitmap(result.bitmap)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Result bitmap is null",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
        }
    }
}