package hk.edu.hkbu.comp.lab01.json

import hk.edu.hkbu.comp.lab01.test.User

data class Login(
        val category_order: List<Any>,
        val fixed_category_list: List<Any>,
        val keyword_filter_list: List<Any>,
        val me: User,
        val token: String,
        val user: User
)