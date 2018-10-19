package hk.edu.hkbu.comp.lab01.json

data class Response<T>(
        val success: Int,
        val server_time: Int,
        val response: T
)