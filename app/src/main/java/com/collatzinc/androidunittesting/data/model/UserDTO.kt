package com.collatzinc.androidunittesting.data.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("maidenName") var maidenName: String? = null,
    @SerializedName("age") var age: Int? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("birthDate") var birthDate: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("bloodGroup") var bloodGroup: String? = null,
    @SerializedName("height") var height: Double? = null,
    @SerializedName("weight") var weight: Double? = null,
    @SerializedName("eyeColor") var eyeColor: String? = null,
    @SerializedName("hair") var hairDTO: HairDTO? = HairDTO(),
    @SerializedName("ip") var ip: String? = null,
    @SerializedName("address") var addressDTO: AddressDTO? = AddressDTO(),
    @SerializedName("macAddress") var macAddress: String? = null,
    @SerializedName("university") var university: String? = null,
    @SerializedName("bank") var bankDTO: BankDTO? = BankDTO(),
    @SerializedName("company") var companyDTO: CompanyDTO? = CompanyDTO(),
    @SerializedName("ein") var ein: String? = null,
    @SerializedName("ssn") var ssn: String? = null,
    @SerializedName("userAgent") var userAgent: String? = null,
    @SerializedName("crypto") var cryptoDTO: CryptoDTO? = CryptoDTO(),
    @SerializedName("role") var role: String? = null
)

data class HairDTO(

    @SerializedName("color") var color: String? = null,
    @SerializedName("type") var type: String? = null

)

data class CoordinatesDTO(

    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lng") var lng: Double? = null

)

data class AddressDTO(

    @SerializedName("address") var address: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("stateCode") var stateCode: String? = null,
    @SerializedName("postalCode") var postalCode: String? = null,
    @SerializedName("coordinates") var coordinatesDTO: CoordinatesDTO? = CoordinatesDTO(),
    @SerializedName("country") var country: String? = null

)

data class BankDTO(

    @SerializedName("cardExpire") var cardExpire: String? = null,
    @SerializedName("cardNumber") var cardNumber: String? = null,
    @SerializedName("cardType") var cardType: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("iban") var iban: String? = null

)

data class CompanyDTO(

    @SerializedName("department") var department: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("address") var addressDTO: AddressDTO? = AddressDTO()

)

data class CryptoDTO(

    @SerializedName("coin") var coin: String? = null,
    @SerializedName("wallet") var wallet: String? = null,
    @SerializedName("network") var network: String? = null

)
