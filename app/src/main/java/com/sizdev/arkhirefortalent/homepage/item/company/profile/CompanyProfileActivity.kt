package com.sizdev.arkhirefortalent.homepage.item.company.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sizdev.arkhirefortalent.R
import com.sizdev.arkhirefortalent.databinding.ActivityCompanyProfileBinding
import com.sizdev.arkhirefortalent.webviewer.ArkhireWebViewerActivity
import com.squareup.picasso.Picasso

class CompanyProfileActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCompanyProfileBinding
    private lateinit var markerDefault: Marker

    private var defaultLocation = LatLng(-6.200000, 106.816666)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_profile)

        // Set Data
        setData()

        // Set Recycler View
        setRecyclerView()

        // Set FAB
        setFloatingButton()

        // Set Button
        setButton()

        // Set Map
        setMap()

    }

    private fun setData() {
        val companyName = intent.getStringExtra("companyName")
        val companyType = intent.getStringExtra("companyType")
        val companyImage = intent.getStringExtra("companyImage")
        val companyLinkedin = intent.getStringExtra("companyLinkedin")
        val companyInstagram = intent.getStringExtra("companyInstagram")
        val companyFacebook = intent.getStringExtra("companyFacebook")
        val companyDesc = intent.getStringExtra("companyDesc")
        val companyLatitude = intent.getStringExtra("companyLatitude")
        val companyLongitude = intent.getStringExtra("companyLongitude")

        defaultLocation = LatLng(companyLatitude!!.toDouble(), companyLongitude!!.toDouble())

        //Set Data
        binding.tvCompanyProfileName.text = companyName
        binding.tvCompanyType.text = companyType
        binding.tvCompanyDescription.text = companyDesc

        when (companyType){
            "Enterprise" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_enterprise)
            "Startup" -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_startup)
            else -> binding.ivCompanyProfileCover.setImageResource(R.drawable.ic_software_house)
        }

        when (companyImage){
            null -> binding.ivCompanyProfileImage.setImageResource(R.drawable.ic_empty_image)
            else -> {
                Picasso.get()
                        .load("http://54.82.81.23:911/image/$companyImage")
                        .resize(512, 512)
                        .centerCrop()
                        .into(binding.ivCompanyProfileImage)
            }
        }

        when(companyLinkedin){
            null -> binding.ivCompanyLinkedIn.setImageResource(R.drawable.ic_linkedin_disabled)
            "" -> binding.ivCompanyLinkedIn.setImageResource(R.drawable.ic_linkedin_disabled)
            else -> {
                binding.ivCompanyLinkedIn.setOnClickListener {
                    val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                    intent.putExtra("url", "http://linkedin.com/company/$companyLinkedin")
                    startActivity(intent)
                }
            }
        }

        when(companyInstagram){
            null -> binding.ivCompanyInstagram.setImageResource(R.drawable.ic_instagram_disabled)
            "" -> binding.ivCompanyInstagram.setImageResource(R.drawable.ic_instagram_disabled)
            else -> {
                binding.ivCompanyInstagram.setOnClickListener {
                    val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                    intent.putExtra("url", "http://instagram.com/$companyInstagram")
                    startActivity(intent)
                }
            }
        }

        when(companyFacebook){
            null -> binding.ivCompanyFacebook.setImageResource(R.drawable.ic_facebook_disabled)
            "" -> binding.ivCompanyFacebook.setImageResource(R.drawable.ic_facebook_disabled)
            else -> {
                binding.ivCompanyFacebook.setOnClickListener {
                    val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                    intent.putExtra("url", "http://facebook.com/$companyFacebook")
                    startActivity(intent)
                }
            }
        }
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setButton() {
        binding.ivHelpLookingFor.setOnClickListener {
            Toast.makeText(this, "Company Criteria Skill Looking For", Toast.LENGTH_SHORT).show()
        }

        binding.ivHelpCompanyDescription.setOnClickListener {
            Toast.makeText(this, "Description About This Company", Toast.LENGTH_SHORT).show()
        }

        binding.ivHelpCompanyLocation.setOnClickListener {
            Toast.makeText(this, "Detail Location Of This Company", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFloatingButton() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.menuButton.setOnClickListener {
            val showMenu = PopupMenu(this, binding.menuButton)
            showMenu.menu.add(Menu.NONE, 0 ,0, "Save Favorite")
            showMenu.menu.add(Menu.NONE, 1 ,1, "Report")
            showMenu.show()

            showMenu.setOnMenuItemClickListener { menuItem ->
                val id = menuItem.itemId

                when (id) {
                    0 -> {Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()}
                    1 -> {Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()}
                }
                false
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvCompanyLookingFor.adapter = CompanyLookingForAdapter()
        binding.rvCompanyLookingFor.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        markerDefault = googleMap.addMarker(
            MarkerOptions()
                .position(defaultLocation)
                .title(binding.tvCompanyProfileName.text as String?)
        )

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        googleMap.setOnMarkerClickListener(this)

        googleMap.setOnCameraMoveStartedListener {
            binding.map.parent.requestDisallowInterceptTouchEvent(true)

        }

        googleMap.setOnCameraIdleListener {
            binding.map.parent.requestDisallowInterceptTouchEvent(false)
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this, "Navigate to ${marker.title} ?", Toast.LENGTH_SHORT).show()
        return false
    }
}