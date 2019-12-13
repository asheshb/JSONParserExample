package com.example.jsonparserexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parse_json.setOnClickListener{
            parseJson()
        }
    }

    private fun parseJson(){
        val users = mutableListOf<User>()
        lifecycleScope.launch {
            val googleData = withContext(Dispatchers.IO) {
                getDataFromNetwork("https://jsonplaceholder.typicode.com/users")
            }

            val jsonArray = JSONArray(googleData)
            for (i in 0 until jsonArray.length()) {
                val user = parseUser(jsonArray.getJSONObject(i))
                users.add(user)
            }

            parsed_data.text = users.joinToString ("\n"){ it.name }
        }

    }
    private fun parseUser(jsonObject: JSONObject): User{
        val addressObject = jsonObject.getJSONObject("address")
        val address = Address(addressObject.getString("street"),
            addressObject.getString("city"),
            addressObject.getString("zipcode"))

            return User(jsonObject.getLong("id"),
            jsonObject.getString("name"),
            jsonObject.getString("email"),
            address)

    }
}
