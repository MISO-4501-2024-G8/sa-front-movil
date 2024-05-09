package com.miso202402.front_miso_pf2_g8_sportapp.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
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
    private var errorTimesLoginRejected: Int = 0
    private val domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLoginFragmentLogin.setOnClickListener {
           makeLogin()
       }

       /* binding.textViewSingUpFragmentLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_RegisterFragment)
        }*/
    }

    override fun onResume() {
        super.onResume()
        // Mostrar la toolbar y el fab en este fragmento
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.hideToolbarAndFab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   private fun makeLogin(){
       val email = binding.editTexEmailAddressFragmentLogin.text.toString()
       val password = binding.editTexPasswordFragmentLogin.text.toString()
       val loginRequest = LoginRequest(
           email,
           password)
       val utils = Utils()
       CoroutineScope(Dispatchers.IO).launch {
           try {
               val callLogin = utils.getRetrofit(domain).create(ApiService::class.java).logIn(loginRequest).execute()
               val loginResponse = callLogin.body() as LoginResponse?
               lifecycleScope.launch {
                   if(errorTimesLoginRejected < 3 ) {
                       if (loginResponse?.message == "Usuario logueado correctamante") {
                           activity?.let { utils.showMessageDialog(it, loginResponse?.message.toString())}
                           //Log.i("mesnaje al loguearse", loginResponse?.message.toString())
                           errorTimesLoginRejected = 0

                           preferences.saveData("token", loginResponse?.token)
                           preferences.saveData("id", loginResponse?.id)

                           val bundle = bundleOf("token" to  loginResponse?.token, "id" to loginResponse?.id)
                           //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
                           val mainActivity = requireActivity() as? MainActivity
                           mainActivity?.navigateToFragment(R.id.CalendarFragment, bundle)
                       }
                       else {
                           errorTimesLoginRejected++
                           val errorToLogin: String = loginResponse?.error.toString()
                           activity?.let { utils.showMessageDialog(it, errorToLogin)}
                           Log.e("error al loguearse", errorToLogin)
                       }
                   } else{
                       val errorMesage: String = "Supero la cantidad de intentos de login"
                       //activity?.let { utils.showMessageDialog(it, errorMesage) }
                       activity?.let { showMessageDialog(it, errorMesage) }
                       Log.e("error al loguearse por cantodad de intentos", errorMesage)
                   }
               }

           } catch (e: Exception) {
               Log.e("error",e.message.toString())
           }
        }
   }

    fun showMessageDialog(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}