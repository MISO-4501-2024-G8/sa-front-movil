package com.miso202402.front_miso_pf2_g8_sportapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentLoginBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLoginFragmentLogin.setOnClickListener {
           makeLogin()
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   private fun makeLogin(){
       val email = binding.editTexEmailAddressFragmentLogin.text.toString()
       val password = binding.editTexPasswordFragmentLogin.text.toString()
       val request = LoginRequest(email, password)
       val utilRetrofit = Utils().getRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            val callLogin = utilRetrofit.create(ApiService::class.java).logIn(request).execute()
            val response = callLogin.body() as LoginResponse?
            lifecycleScope.launch {
                if (response?.message == "Usuario logueado correctamante") {
                    showMessageDialog(response?.message.toString())
                } else {
                    showMessageDialog(response?.error.toString())
                }
            }
        }
    }

    private fun showMessageDialog(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}