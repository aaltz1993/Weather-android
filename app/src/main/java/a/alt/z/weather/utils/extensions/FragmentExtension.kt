package a.alt.z.weather.utils.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

fun Fragment.showToast(duration: Int = Toast.LENGTH_SHORT, text: (() -> String)) {
    Toast.makeText(requireContext(), text(), duration).show()
}

fun Fragment.setFragmentResult(requestKey: String, key: String, result: Any) {
    parentFragmentManager.setFragmentResult(
        requestKey,
        bundleOf(key to result)
    )
}