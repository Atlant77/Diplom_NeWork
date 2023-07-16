package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.installations.Utils
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentLoginBinding
import ru.netology.nework.repository.AuthRepository
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.view.LogInViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    @Inject
    lateinit var repository: AuthRepository

    private val viewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Выполнить вход"

        binding.loginButton.setOnClickListener {
            val login = binding.login.editText.toString()
            val pass = binding.password.editText.toString()
            if (binding.login.editText == null || binding.password.editText == null) {
                Toast.makeText(
                    activity,
                    "Please, fill all text",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.logIn(login, pass)
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }


//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}