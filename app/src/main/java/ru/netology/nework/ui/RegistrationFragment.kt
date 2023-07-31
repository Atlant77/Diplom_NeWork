package ru.netology.nework.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentRegistrationBinding
import ru.netology.nework.repository.AuthRepository
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var repository: AuthRepository

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        with(binding) {
            val pickPhotoLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    when (it.resultCode) {
                        ImagePicker.RESULT_ERROR -> {
                            Snackbar.make(
                                binding.root,
                                ImagePicker.getError(it.data),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        Activity.RESULT_OK -> {
                            val uri: Uri? = it.data?.data
                            viewModel.changeAvatar(uri, uri?.toFile())
                        }
                    }
                }

            binding.makePhoto.setOnClickListener {
                ImagePicker.with(requireActivity())
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.CAMERA)
                    .createIntent(pickPhotoLauncher::launch)
            }

            binding.selectPhoto.setOnClickListener {
                ImagePicker.with(requireActivity())
                    .crop()
                    .compress(2048)
                    .provider(ImageProvider.GALLERY)
                    .galleryMimeTypes(
                        arrayOf(
                            "image/png",
                            "image/jpeg",
                        )
                    )
                    .createIntent(pickPhotoLauncher::launch)
            }

            registrationButton.setOnClickListener {
                AndroidUtils.hideKeyboard(requireView())
                viewModel.registration(
                    binding.loginInput.text.toString(),
                    binding.passwordInput.text.toString(),
                    binding.nameInput.text.toString()
                )
            }
        }

        viewModel.avatar.observe(viewLifecycleOwner) {
            if (it.uri != null) {
                Glide.with(binding.avatar)
                    .load(it.uri)
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .timeout(10_000)
                    .transform(MultiTransformation(FitCenter(), CircleCrop()))
                    .into(binding.avatar)
                binding.removePhoto.isVisible = true
            } else {
                binding.avatar.setImageResource(R.drawable.baseline_account_circle_24)
                binding.removePhoto.isVisible = false
            }
        }

        binding.removePhoto.setOnClickListener {

        }

        viewModel.authorizationData.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            auth.setAuth(it.id, it.token)
            findNavController().navigateUp()
        }

        binding.goToLoginButton.setOnClickListener {
            findNavController().navigate(R.id.logInFragment)
        }

        return binding.root
    }
}