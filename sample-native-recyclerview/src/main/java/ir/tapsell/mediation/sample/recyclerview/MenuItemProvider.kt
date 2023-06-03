package ir.tapsell.mediation.sample.recyclerview

import android.content.Context
import android.util.Log
import ir.tapsell.mediation.sample.recyclerview.ui.MainActivity
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * A singleton providing the list items
 */
object MenuItemProvider {
    /**
     * Adds [MenuItem]'s from a JSON file.
     */
    internal fun fetchMenuItems(context: Context): List<MenuItem> {
        val list = mutableListOf<MenuItem>()
        try {
            val jsonDataString = readJsonDataFromFile(context)
            val menuItemsJsonArray = JSONArray(jsonDataString)
            for (i in 0 until menuItemsJsonArray.length()) {
                val menuItemObject = menuItemsJsonArray.getJSONObject(i)
                val menuItemName = menuItemObject.getString("name")
                val menuItemDescription = menuItemObject.getString("description")
                val menuItemPrice = menuItemObject.getString("price")
                val menuItemCategory = menuItemObject.getString("category")
                val menuItemImageName = menuItemObject.getString("photo")
                val menuItem = MenuItem(
                    menuItemName, menuItemDescription, menuItemPrice,
                    menuItemCategory, menuItemImageName
                )
                list.add(menuItem)
            }
        } catch (exception: IOException) {
            Log.e(MainActivity::class.java.name, "Unable to parse JSON file.", exception)
        } catch (exception: JSONException) {
            Log.e(MainActivity::class.java.name, "Unable to parse JSON file.", exception)
        }

        return list
    }

    /**
     * Reads the JSON file and converts the JSON data to a [String].
     *
     * @return A [String] representation of the JSON data.
     * @throws IOException if unable to read the JSON file.
     */
    @Throws(IOException::class)
    private fun readJsonDataFromFile(context: Context): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonDataString: String?
            inputStream = context.resources.openRawResource(R.raw.menu_items_json)
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonDataString = it } != null) {
                builder.append(jsonDataString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }
}