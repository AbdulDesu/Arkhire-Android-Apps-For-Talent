package com.sizdev.arkhirefortalent.homepage.item.account.profile.previewer

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.sizdev.arkhirefortalent.R
import com.sizdev.arkhirefortalent.administration.login.LoginActivity
import com.sizdev.arkhirefortalent.databinding.ActivityTalentPreviewBinding
import com.sizdev.arkhirefortalent.homepage.item.account.profile.TalentProfileContract
import com.sizdev.arkhirefortalent.homepage.item.account.profile.TalentProfilePresenter
import com.sizdev.arkhirefortalent.networking.ArkhireApiClient
import com.sizdev.arkhirefortalent.networking.ArkhireApiService
import com.sizdev.arkhirefortalent.webviewer.ArkhireWebViewerActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.alert_call_confirmation.view.*
import kotlinx.android.synthetic.main.alert_mailing_confirmation.view.*
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class TalentPreviewActivity : AppCompatActivity(), TalentProfileContract.View {

    private lateinit var binding: ActivityTalentPreviewBinding
    private lateinit var pagerAdapter: TalentProfilePreviewTabAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    private lateinit var coroutineScope: CoroutineScope

    private var talentID: String? =null
    private var phoneNumber: String? = null
    private var emailAddress: String? = null
    private var presenter: TalentProfilePresenter? = null

    companion object {
        private const val PERMISSION_CODE = 911
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_talent_preview)

        // Set Service
        setService()

        // Privacy Manager
        setPrivacy()

        // Set Floating Button
        setFloatingButton()

        // Show Progress Bar
        showProgressBar()

        // Set Profile Tab
        setProfileTab()

        // Set Data Refresh Manager
        setRefreshManager()

    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun setFloatingButton() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.menuButton.setOnClickListener {
            val showMenu = PopupMenu(this, binding.menuButton)
            val talentCv = intent.getStringExtra("talentCv")
            showMenu.menu.add(Menu.NONE, 0 ,0, "Show CV")
            showMenu.menu.add(Menu.NONE, 1 ,1, "Report Talent")
            showMenu.show()

            showMenu.setOnMenuItemClickListener { menuItem ->
                val id = menuItem.itemId
                when (id) {
                    0 -> {
                        val intent = Intent(this, ArkhireWebViewerActivity::class.java)
                        intent.putExtra("webScale", "100")
                        intent.putExtra("url", "http://54.82.81.23:911/image/$talentCv")
                        startActivity(intent)
                    }
                    1 -> Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                sessionExpiredAlert()
                dialog.show()
            }
            else -> {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = this.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = TalentProfilePresenter(coroutineScope, service)

        talentID = intent.getStringExtra("talentID")
    }

    override fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    override fun getSavedData() {
        talentID = intent.getStringExtra("talentID")
    }

    override fun setTalentData(talentID: String?, accountID: String?, accountName: String?, accountEmail: String?, accountPhone: String?, talentTitle: String?, talentTime: String?, talentCity: String?, talentDesc: String?, talentImage: String?, talentGithub: String?, talentCv: String?, talentSkill1: String?, talentSkill2: String?, talentSkill3: String?, talentSkill4: String?, talentSkill5: String?) {
        binding.tvProfileTalentName.text = accountName
        binding.tvProfileTalentTitle.text = talentTitle
        binding.tvProfileTalentDesc.text = talentDesc
        binding.tvProfileTalentLocation.text =  talentCity

        phoneNumber = accountPhone
        emailAddress = accountEmail

        Picasso.get()
                .load("http://54.82.81.23:911/image/$talentImage")
                .resize(512, 512)
                .centerCrop()
                .into(binding.ivTalentImageProfile)

        if (talentTime == "Freelance") {
            binding.ivTalentProfileCover.setImageResource(R.drawable.ic_freelancer)
            binding.btWorkTime.setImageResource(R.drawable.ic_time_freelancer)
            binding.btWorkTime.setOnClickListener {
                Toast.makeText(this, "This Talent Is Freelancer", Toast.LENGTH_LONG).show()
            }
        } else {
            binding.ivTalentProfileCover.setImageResource(R.drawable.ic_fulltimework)
            binding.btWorkTime.setImageResource(R.drawable.ic_worktime)
            binding.btWorkTime.setOnClickListener {
                Toast.makeText(this, "This Talent Is Full Time Worker", Toast.LENGTH_LONG).show()
            }
        }

        //Validate Skill Null or not
        if (talentSkill1 != null) {
            binding.tvTitleProfileTalentSkill1.text = talentSkill1
        } else {
            binding.tvTitleProfileTalentSkill1.visibility = View.INVISIBLE
        }

        if (talentSkill2 != null) {
            binding.tvTitleProfileTalentSkill2.text = talentSkill2
        } else {
            binding.tvTitleProfileTalentSkill2.visibility = View.INVISIBLE
        }

        if (talentSkill3 != null) {
            binding.tvTitleProfileTalentSkill3.text = talentSkill3
        } else {
            binding.tvTitleProfileTalentSkill3.visibility = View.INVISIBLE
        }

        if (talentSkill4 != null) {
            binding.tvTitleProfileTalentSkill4.text = talentSkill4
        } else {
            binding.tvTitleProfileTalentSkill4.visibility = View.INVISIBLE
        }

        if (talentSkill5 != null) {
            binding.tvTitleProfileTalentSkill5.text = talentSkill5
        } else {
            binding.tvTitleProfileTalentSkill5.visibility = View.INVISIBLE
        }

        binding.ivTalentPhone.setOnClickListener{
            startAlertCallConfirmation()
            dialog.show()
        }

        binding.ivTalentEmail.setOnClickListener {
            startAlertEmailConfirmation()
            dialog.show()
        }

        binding.ivTalentGithub.setOnClickListener {
            val intent = Intent(this, ArkhireWebViewerActivity::class.java)
            if (talentGithub != "null"){
                intent.putExtra("url", "https://github.com/$talentGithub")
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "This talent not have github account", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun sessionExpiredAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_session_expired, null)

        dialog = this.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }

        view.bt_okRelog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            val sharedPref = this.getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            finish()
        }
    }

    override fun setProfileTab() {
        pagerAdapter = TalentProfilePreviewTabAdapter(supportFragmentManager)
        binding.vpTalentProfile.adapter = pagerAdapter
        binding.tabTalentProfile.setupWithViewPager(binding.vpTalentProfile)
    }

    override fun setRefreshManager() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getTalentData(talentID!!)
                handler.postDelayed(this, 2000)
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun startAlertEmailConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_mailing_confirmation, null)
        dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()

        view.tv_alertEmailAddressTalent.text = emailAddress

        view.bt_yesSend.setOnClickListener {
            dialog.dismiss()
            val sendEmail = Intent(Intent.ACTION_SENDTO)
            sendEmail.putExtra(Intent.EXTRA_EMAIL, emailAddress)
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Arkhire Email")
            sendEmail.data = Uri.parse("mailto: $emailAddress")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_FROM_BACKGROUND)
            try {
                startActivity(sendEmail)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Log.d("Email error:", e.toString())
            }
        }

        view.bt_noSend.setOnClickListener {
            dialog.cancel()
        }
    }

    @SuppressLint("InflateParams")
    private fun startAlertCallConfirmation() {
        val view: View = layoutInflater.inflate(R.layout.alert_call_confirmation, null)

        dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()

        view.tv_alertNumberTalent.text = phoneNumber
        view.bt_yesCall.setOnClickListener {
            dialog.dismiss()
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.CALL_PHONE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                //permission already granted
                callTalent(phoneNumber!!)
            }
        }

        view.bt_noCall.setOnClickListener {
            dialog.cancel()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    callTalent(phoneNumber!!)
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun callTalent(talentPhone: String) {
        val call = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", talentPhone, null))
        try {
            startActivity(call )
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun setPrivacy() {
        when(intent.getStringExtra("previewCode")) {
            "guest" -> {
                binding.ivTalentPhone.visibility = View.GONE
                binding.ivTalentEmail.visibility = View.GONE
                binding.btWorkTime.visibility = View.GONE
                binding.menuButton.visibility = View.GONE
            }

            "owner" -> {
                Toast.makeText(this, "Showing Your Profile..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}