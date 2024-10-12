package com.example.decodingevents.ui.detail_events

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.decodingevents.data.resource.DetailEventResponse
import com.example.decodingevents.databinding.ActivityDetailEventBinding
import com.example.decodingevents.ui.EventsViewModel


class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val detailEventViewModel by viewModels<EventsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id_key")

        id?.let { detailEventViewModel.getEventById(it) }

        detailEventViewModel.detailEvent.observe(this) { detail ->
            setDetail(detail)
        }

        detailEventViewModel.isError.observe(this) {
            error -> setError(error)
        }

        detailEventViewModel.isLoading.observe(this) {
            loading -> setLoading(loading)
        }
    }

    private fun setLoading(loading: Boolean) {
        if(!loading) {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setError(error: Boolean) {
        if (error) {
            Toast.makeText(this, "${detailEventViewModel.errorMessage}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setDetail(detail: DetailEventResponse) {

        Glide.with(binding.imgLogo.context).load(detail.event.imageLogo).into(binding.imgLogo)
        binding.apply {
            tvEventName.text = detail.event.name
            tvEventOwnerName.text = detail.event.ownerName
            tvEventDate.text = detail.event.beginTime
            tvEventAvailableSeats.text = (detail.event.quota - detail.event.registrants).toString()
            tvEventDescription.text = HtmlCompat.fromHtml(
                detail.event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }
}