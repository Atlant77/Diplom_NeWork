package ru.netology.nework.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.activity_app) {

    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var auth: AppAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = supportActionBar

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
        }

        authViewModel.data.observe(this) {
            if (authViewModel.authorized) {
                actionBar?.title = authViewModel.authUser.value?.name
//                auth.authStateFlow.value.id.toString()
                actionBar?.setDisplayShowHomeEnabled(true)
                actionBar?.setDisplayUseLogoEnabled(true)
                actionBar?.setLogo(R.drawable.baseline_account_circle_24)

//                actionBar?.setLogo(
//                    Glide.with(this)
//                        .asDrawable()
//                        .load(authViewModel.authUser.value?.avatar)
//                        .placeholder(R.drawable.baseline_downloading_24)
//                        .error(R.drawable.baseline_broken_image_24)
//                        .timeout(10_000)
//                )
//
//                Glide.with(this)
//                    .asBitmap()
//                    .load(authViewModel.authUser.value?.avatar)
//                    .placeholder(R.drawable.baseline_downloading_24)
//                    .error(R.drawable.baseline_broken_image_24)
//                    .timeout(10_000)
//                    .into(object : CustomTarget<Drawable>() {
//                        override fun onResourceReady(
//                            resource: Drawable,
//                            transition: Transition<in Drawable>?
//                        ) {
//                            actionBar?.setLogo(resource)
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            // this is called when imageView is cleared on lifecycle call or for
//                            // some other reason.
//                            // if you are referencing the bitmap somewhere else too other than this imageView
//                            // clear it here as you can no longer have the bitmap
//                        }
//                    })
            } else {
                actionBar?.setDisplayShowHomeEnabled(true)
                actionBar?.setDisplayUseLogoEnabled(true)
                actionBar?.setLogo(R.drawable.baseline_account_circle_24)
                actionBar?.title = "Not login"
            }

            invalidateOptionsMenu()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("some stuff happened: ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            println(token)
        }

        checkGoogleApiAvailability()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.let {
            it.setGroupVisible(R.id.unauthenticated, !authViewModel.authorized)
            it.setGroupVisible(R.id.authenticated, authViewModel.authorized)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_in -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_logInFragment)
                true
            }
            R.id.registr -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_registrationFragment)
                true
            }
            R.id.logout -> {
                auth.removeAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }
    }
}