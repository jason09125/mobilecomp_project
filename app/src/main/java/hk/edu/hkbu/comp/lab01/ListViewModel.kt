package hk.edu.hkbu.comp.lab01

import android.databinding.ObservableArrayList
import android.support.annotation.LayoutRes
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ListViewModel<T>(variableId:Int, @LayoutRes layoutRes:Int) {
    val items = ObservableArrayList<T>()
    val itemBinding = ItemBinding.of<T>(variableId, layoutRes)
}