package hk.edu.hkbu.comp.lab01.json

data class SubCategory(
        val sub_cat_id: String,
        val cat_id: String,
        val name: String,
        val postable: Boolean,
        val filterable: Boolean,
        val orderable: Boolean,
        val is_filter: Boolean,
        val url: String,
        val query: Query
)