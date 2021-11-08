package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemAddressBinding
import a.alt.z.weather.model.location.Address
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AddressViewHolder(
    private val binding: ItemAddressBinding,
    private val onClickAction: (Address) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(address: Address) {
        binding.apply {
            if (address == Address.INVALID) {
                root.setOnClickListener(null)
                addressTextView.text = root.context.getString(R.string.no_search_result)
                // addressTextView.setTextColor(ContextCompat.getColor(root.context, R.color.))
            } else {
                root.setOnClickListener { onClickAction(address) }
                addressTextView.text = address.address
            }
        }
    }
}