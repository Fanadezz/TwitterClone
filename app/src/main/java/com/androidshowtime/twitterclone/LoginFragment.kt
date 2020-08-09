package com.androidshowtime.twitterclone


import android.R
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidshowtime.twitterclone.databinding.FragmentLoginBinding
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class LoginFragment : Fragment(), View.OnKeyListener, View.OnClickListener {

    private var signUpMode = false
    private lateinit var loginButton: Button
    private lateinit var user: ParseUser
    private lateinit var viewModel: MyViewModel
    private lateinit var passwordEdt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //helps to see how much the user is using our app
        // ParseAnalytics.trackAppOpenedInBackground(getIntent())

        user = ParseUser()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        passwordEdt = binding.passwordEdt

        val userNamesArray = arrayOf(
            "Kago",
            "David",
            "Patricia",
            "Cash",
            "Racheal",
            "Warubaga",
            "Adam",
            "Ex",
            "Mikosi",
            "Njambi",
            "Manchester",
            "Kim",
            "Goat",
            "Dan",
            "Dom",
            "Ace"
                                    )

        //create arrayAdapter
        val adapter =
                ArrayAdapter<String>(requireActivity(), R.layout.simple_list_item_1, userNamesArray)

        //set the AutoComplete EditText to the Adapter
        binding.userNameEdt.setAdapter(adapter)

        //set character number to show prompts
        binding.userNameEdt.threshold = 1






        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        loginButton = binding.loginButton
        // setting the fragment as the lifecycle owner
        binding.lifecycleOwner = this

        /*set ViewModel for Data Binding - this allows the layout to access all data in the
          ViewModel*/
        binding.viewModel = viewModel

        //inserting fragment title on the ActionBar
        (activity as AppCompatActivity).supportActionBar?.title = "Twitter: Login"


        binding.loginButton.setOnClickListener {

            val username = userNameEdt.text.toString()
            val password = passwordEdt.text.toString()


            if (isCredentialsBlank(username, password)) {

                Toast.makeText(activity, "Username and Password Required", Toast.LENGTH_SHORT)
                        .show()
            }
            else {

                loginCall(username, password)

            }


        }



        binding.constraintLayout.setOnClickListener(this)
        passwordEdt.setOnKeyListener(this)
        redirectUser()
        return binding.root


    }


    fun isCredentialsBlank(userName: String, password: String): Boolean {

        return (userName == "") || (password == "")

    }

    //for login we use the static ParseUser
    fun loginCall(userName: String, password: String) {


        ParseUser.logInInBackground(userName, password) { user: ParseUser?, e: ParseException? ->
            if (e == null) {
                Timber.i("Login Successful")
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT)
                        .show()
                redirectUser()

            }
            else {


                if (e.message == "Invalid username/password.") {

                    signUpCall(userName, password)

                }
                else {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()

                    Timber.i("Login Failure ${e.message}")
                }


            }

        }


    }


    //for signUp we use the actual user
    fun signUpCall(userName: String, password: String) {

        //setting up users credentials
        user.username = userName
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null) {
                Toast.makeText(activity, "signedUp successfully", Toast.LENGTH_SHORT)
                        .show()

                redirectUser()
            }
            else {

                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
            }

        }

    }


    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        Timber.i("Entering Key event")

        //keycode tells us what kind of key event was pressed
        if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {

            //loginButton.callOnClick()
            loginButton.performClick()
        }

        return false
    }

    override fun onClick(view: View?) {
        val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager

        //use the view to get the token
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    fun redirectUser() {


        if (ParseUser.getCurrentUser() != null) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToFollowersFragment())
        }
    }


}