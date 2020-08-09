package com.androidshowtime.twitterclone

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.twitterclone.databinding.FragmentFollowersBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parse.ParseObject
import com.parse.ParseUser
import timber.log.Timber


class FollowersFragment : Fragment() {
    private lateinit var users: MutableList<String>
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        users = mutableListOf()
        val binding = FragmentFollowersBinding.inflate(inflater)

        val list = mutableListOf("Tony", "Kibe", "Pitaa", "Mosee")
        val adapter =
                ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_checked, users)
        listView = binding.listView
        getUsers(adapter)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, l ->

            val checkedTextView = view as CheckedTextView

            if (checkedTextView.isChecked) {

                ParseUser.getCurrentUser()
                        .add("followers", users[i])

            }
            else {

                ParseUser.getCurrentUser()
                        .getList<String>("followers")
                        .remove(users[i])

                val tempListView = ParseUser.getCurrentUser()
                        .getList<String>("followers")

                ParseUser.getCurrentUser()
                        .getList<String>("followers")
                        .remove("followers")
                ParseUser.getCurrentUser()
                        .put("followers", tempListView)
            }

            ParseUser.getCurrentUser()
                    .saveInBackground()

        }




        (activity as AppCompatActivity).supportActionBar?.title =
                ("${ParseUser.getCurrentUser().username} Pals ")

        setHasOptionsMenu(true)

        return binding.root
    }


    fun getUsers(adapter: ArrayAdapter<String>) {
        val query = ParseUser.getQuery()

        //hiding the current user
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().username)

        //sorting users
        query.orderByAscending("username")

        query.findInBackground { objects, e ->

            if (e == null && objects.size > 0) {
                objects.forEach { user ->
                    users.add(user.username)


                }
                adapter.notifyDataSetChanged()



                users.forEachIndexed {

                        i, user ->

                    if (ParseUser.getCurrentUser()
                                    .getList<String>("followers")
                                    .contains(user)) {
                        listView.setItemChecked(i, true)
                    }
                }
            }
            else {

                Timber.i("Users Error")
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.newTweet -> {
                sendTweet()


            }
            R.id.timeline -> {
                findNavController().navigate(
                    FollowersFragmentDirections.actionFollowersFragmentToTimelineFragment())

            }
            R.id.logout -> {
                findNavController().navigate(
                    FollowersFragmentDirections.actionFollowersFragmentToLoginFragment())
                ParseUser.logOutInBackground()

            }
        }



        return super.onOptionsItemSelected(item)
    }

    fun sendTweet() {
        val editText = EditText(requireContext())


        val materialAlertDialogBuilder =
                MaterialAlertDialogBuilder(requireContext()).setTitle("Send a Tweet")
                        .setView(editText)
                        .setPositiveButton("Send Tweet") { dialogInterface, i ->
                            val tweet = editText.text.toString()
                            if (tweet.isNotBlank()) {
                                val parseObject = ParseObject("Tweet")
                                parseObject.put("tweet", tweet)

                                parseObject.put("username", ParseUser.getCurrentUser().username)
                                parseObject.saveInBackground { e ->

                                    if (e == null) {
                                        Toast.makeText(requireContext(), "Tweet Sent",
                                                       Toast.LENGTH_SHORT)
                                                .show()
                                    }
                                    else {
                                        Toast.makeText(requireContext(), "Failed", Toast
                                                .LENGTH_SHORT)
                                                .show()
                                        Toast.makeText(requireContext(), e.message,
                                                       Toast.LENGTH_SHORT)
                                                .show()
                                    }
                                }
                            }
                            else
                                Toast.makeText(requireContext(), "Empty Tweet", Toast
                                        .LENGTH_SHORT)
                                        .show()

                        }
                        .setNegativeButton("Cancel") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }


        materialAlertDialogBuilder.show()


    }


}