package hk.edu.hkbu.comp.lab01

import android.databinding.ObservableField

class ProfileViewModel(
        val email: ObservableField<String> = ObservableField(""),
        val displayName: ObservableField<String> = ObservableField(""),
        val phoneNumber: ObservableField<String> = ObservableField(""),
        val photoUrl: ObservableField<String> = ObservableField(""),
        val password:ObservableField<String> = ObservableField(""),
        val passwordConfirm:ObservableField<String> = ObservableField("")
)