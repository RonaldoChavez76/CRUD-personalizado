package mx.edu.utng.crudtelefonos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhoneAdapter(
    private var phoneList: ArrayList<PhoneModelClass>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder>(), Filterable {

    // Lista completa para la búsqueda
    private var phoneListFull = ArrayList(phoneList)

    inner class PhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvBrand: TextView = itemView.findViewById(R.id.tvBrand)
        val tvModel: TextView = itemView.findViewById(R.id.tvModel)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_phone, parent, false)
        return PhoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val phone = phoneList[position]
        holder.tvBrand.text = phone.brand
        holder.tvModel.text = phone.model
        holder.tvPrice.text = "$${phone.price}"
    }

    override fun getItemCount(): Int = phoneList.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // Implementación del filtrado
    override fun getFilter(): Filter {
        return phoneFilter
    }

    private val phoneFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<PhoneModelClass>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(phoneListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in phoneListFull) {
                    if (item.brand.toLowerCase().contains(filterPattern) ||
                        item.model.toLowerCase().contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            phoneList.clear()
            phoneList.addAll(results?.values as ArrayList<PhoneModelClass>)
            notifyDataSetChanged()
        }
    }
}