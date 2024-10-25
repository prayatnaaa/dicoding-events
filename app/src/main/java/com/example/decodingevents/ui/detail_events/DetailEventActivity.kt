package com.example.decodingevents.ui.detail_events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.decodingevents.data.Result
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.databinding.ActivityDetailEventBinding
import com.example.decodingevents.ui.EventViewModelFactory
import com.example.decodingevents.ui.events.EventsViewModel


class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    companion object {
        const val ID_KEY = "id_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventFactory: EventViewModelFactory =
            EventViewModelFactory.getInstance(this)
        val detailEventViewModel: EventsViewModel by viewModels {
            eventFactory
        }

        val id = intent.getStringExtra(ID_KEY)

        detailEventViewModel.getEventById(id!!).observe(this) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setDetail(result.data)
                    }
                }
            }
        }

    }

    private fun setDetail(detail: Event) {

        Glide.with(binding.imgLogo.context).load(detail.imageLogo).into(binding.imgLogo)
        binding.apply {
            tvEventName.text = detail.name
            tvEventOwnerName.text = detail.ownerName
            tvEventDate.text = detail.beginTime
            tvEventAvailableSeats.text = (detail.quota - detail.registrants).toString()
            tvEventDescription.text = HtmlCompat.fromHtml(
                detail.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(detail.link)
            }
            startActivity(intent)
        }
    }
}