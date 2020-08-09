package com.androidshowtime.twitterclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidshowtime.twitterclone.databinding.FragmentTimelineBinding
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import timber.log.Timber

class TimelineFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: SimpleAdapter
    private lateinit var tweetsMap: MutableMap<String, String>
    private lateinit var tweetsListData: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        val binding = FragmentTimelineBinding.inflate(inflater)



        listView = binding.listView

        showTweets()
        /*val tweetData = mutableListOf<MutableMap<String, String>>()

        for (i in 1..5) {

            val tweetInfo = mutableMapOf<String, String>()

            tweetInfo.put("content", "Tweeet Content $i")
            tweetInfo.put("username", "User $i")

            tweetData.add(tweetInfo)
        }

        val simpleAdapter = SimpleAdapter(requireContext(), tweetData, android.R.layout
                .simple_list_item_2, arrayOf("content", "username"),
                                          intArrayOf(android.R.id.text1, android.R.id.text2))

        listView.adapter = simpleAdapter*/

        (activity as AppCompatActivity).supportActionBar?.title = "${ParseUser.getCurrentUser()
                .username}'s Timeline"


        return binding.root
    }


    fun showTweets() {
        // val query = ParseQuery<ParseObject>("Tweet")
        val query = ParseQuery.getQuery<ParseObject>("Tweet")


        query.whereContainedIn("username", ParseUser.getCurrentUser()
                .getList("followers"))
        query.orderByDescending("createdAt")
        //  query.limit = 20
        query.findInBackground { objects, e ->


            if (e == null) {

                tweetsListData = mutableListOf()
                objects.forEach { parseObject ->

                    tweetsMap = mutableMapOf()
                    tweetsMap.put("content", parseObject.getString("tweet"))

                    tweetsMap.put("username", parseObject.getString("username"))

                    tweetsListData.add(tweetsMap)
                    Timber.i(tweetsListData.toString())
                }



                adapter = SimpleAdapter(requireContext(), tweetsListData, android.R.layout
                        .simple_list_item_2, arrayOf("content", "username"),
                                        intArrayOf(android.R.id.text1, android.R.id.text2))
                listView.adapter = adapter

            }
            else {

                Toast.makeText(requireContext(), "error occurred ", Toast
                        .LENGTH_SHORT)
                        .show()
            }
        }


    }


}