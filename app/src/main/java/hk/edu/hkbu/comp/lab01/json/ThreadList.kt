package hk.edu.hkbu.comp.lab01.json

data class ThreadList(
        val category: Category,
        val is_pagination: Boolean,
        val items: List<Thread>
)