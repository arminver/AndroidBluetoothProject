package s9.projetandroid.helloble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ExempleAdapter(
    private val stringList: Array<String>,
    private val onClick: ((selectedString: String) -> Unit)? = null
) : RecyclerView.Adapter<ExempleAdapter.ViewHolder>() {

    // Comment s'affiche ma vue
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun showItem(string: String, onClick: ((selectedString: String) -> Unit)? = null) {
            itemView.findViewById<TextView>(R.id.title).text = string
            if (onClick != null) {
                itemView.setOnClickListener {
                    onClick(string)
                }
            }
        }
    }

    // Retourne une « vue » / « layout » pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    // Connect la vue ET la données
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showItem(stringList[position], onClick)
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

}
