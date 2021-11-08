package a.alt.z.weather.ui.location

import a.alt.z.weather.databinding.ItemAddressBinding
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class AddressAdapter(
    private val onClickAction: (Address) -> Unit
): ListAdapter<Address, AddressViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return ItemAddressBinding
            .inflate(parent.layoutInflater, parent, false)
            .let { AddressViewHolder(it, onClickAction) }
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem.address == newItem.address
            }

            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem.address == newItem.address
            }
        }
    }
}