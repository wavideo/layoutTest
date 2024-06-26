package com.example.layouttest

import android.os.Parcel
import android.os.Parcelable

data class Account (
    var id: String,
    var pw: String,
    var name: String = "",
    var team: Int = 0,
    var teamNum: Int = 0,
    var mbti: String = "" ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?:"",
        parcel.readInt()?:0,
        parcel.readInt()?:0,
        parcel.readString()?:"" )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(pw)
        parcel.writeString(name)
        parcel.writeInt(team)
        parcel.writeInt(teamNum)
        parcel.writeString(mbti)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}



object AccountManager {
    // 가입한 Account 리스트
    val accounts = mutableListOf<Account>()

    // Account 인스턴스 추가하기
    fun add(instance:Account){
        accounts.add(instance)
    }

    // 리스트에서 인스턴스 찾기
    fun find(_id: String): Account? {
        return accounts.find { it.id == _id }
    }

    // 테스트  등록
    fun getTest() {
        add(Account("test", "test"))
        find("test")?.name = "김은택"
        find("test")?.team = 1
        find("test")?.teamNum = 1
        find("test")?.mbti = "ENTP"

    }
}