package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemAddressBinding
import a.alt.z.weather.model.location.Address
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AddressViewHolder(
    private val binding: ItemAddressBinding,
    private val onClickAction: (Address) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(address: Address, query: String) {
        binding.apply {
            if (address == Address.INVALID) {
                root.setOnClickListener(null)
                addressTextView.text = root.context.getString(R.string.no_search_result)
            } else {
                root.setOnClickListener { onClickAction(address) }

                val start = address.address.indexOf(query)
                val end = start + query.length
                val spannableString = SpannableString(address.address)

                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(root.context, R.color.address_result_text_highlight_color)),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                addressTextView.text = spannableString
            }
        }
    }
}