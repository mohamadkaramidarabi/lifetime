package com.example.lifetime.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.lifetime.R
import java.util.ArrayList





class AutoCompleteCountryAdapter(private val mContext: Context, @LayoutRes private val viewResourceId: Int, private val items: ArrayList<String>) : ArrayAdapter<String>(mContext, viewResourceId, items) {
    private var countryAll = ArrayList<String>()
    private var countries = ArrayList<String>()
    private var suggestions = ArrayList<String>()


    companion object {
        const val TAG="AutoCompleteCountryAdapter"
    }

    init {
        this.countries = items
        this.countryAll = items.clone() as ArrayList<String>
        suggestions = ArrayList()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        var v= convertView

        try {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(viewResourceId,parent,false)
            }
            v?.findViewById<TextView>(R.id.tvCountry)?.text = countries[position]


            /*if (position < countries.size -1){
                val a = countries[position].name?.substring(0,1)  ?:   ""
                val b = countries[position+1].name?.substring(0,1) ?: ""
                if (a==b){
                    v?.findViewById<View>(R.id.divider)?.visibility = View.INVISIBLE
                }else{
                    v?.findViewById<View>(R.id.divider)?.visibility = View.VISIBLE
                }
            }*/

        } catch (e: Exception) {
            e.printStackTrace()

        }

        return v
    }

    override fun getCount(): Int {

        return countries.size
    }
    override fun getItem(position: Int): String? {

        return countries[position]
    }

    override fun getFilter(): Filter {
        return countryFilter

    }
    private var countryFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String {
            return (resultValue as? String) ?: ""
        }

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val filterResults = FilterResults()
            return if (constraint != null) {
                suggestions.clear()
                for(country in countryAll){
                    if (country?.toLowerCase()?.startsWith(constraint.toString().toLowerCase()) == true ) {
                        suggestions.add(country)
                    }
                }
                filterResults.values = suggestions
                filterResults.count  = suggestions.size
                filterResults
            } else {
                Filter.FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
            val filteredList = results?.values as? ArrayList<String>
            if (results != null && results.count > 0) {
                clear()
                for (c in filteredList!!) {
                    add(c)
                }
                notifyDataSetChanged()
            }
        }
    }


}
