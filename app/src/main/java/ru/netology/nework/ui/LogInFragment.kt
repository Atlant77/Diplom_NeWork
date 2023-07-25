package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentLoginBinding
import ru.netology.nework.repository.AuthRepository
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var repository: AuthRepository

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Выполнить вход"

        binding.loginButton.setOnClickListener {
            val login = binding.loginInput.text.toString()
            val pass = binding.passwordInput.text.toString()
            if (binding.loginInput.text.isNullOrBlank() || binding.passwordInput.text.isNullOrBlank()) {
                Toast.makeText(
                    activity,
                    getString(R.string.please_fill_all_fields),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.authorization(login, pass)
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }

        viewModel.authorizationData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            auth.setAuth(it.id, it.token)
        }
        return binding.root
    }
}