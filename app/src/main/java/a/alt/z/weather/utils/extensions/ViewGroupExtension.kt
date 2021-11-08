package a.alt.z.weather.utils.extensions

import android.view.LayoutInflater
import android.view.ViewGroup

val ViewGroup.layoutInflater get(): LayoutInflater = LayoutInflater.from(context)