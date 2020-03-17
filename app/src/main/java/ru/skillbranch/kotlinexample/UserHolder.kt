package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.util.regex.Matcher
import java.util.regex.Pattern

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ):User{
        return User.makeUser(fullName, email = email, password = password)
            .also { user-> map[user.login] = user
            println("User registered by login: ${user.login}")}
    }

    fun loginUser(login: String, password: String) : String? {
        //return map.toString()
        var _login = ""
        if(checkPhone(login) == false){
            _login = login.trim()
        }else{
            //login is phone
            _login =
                phoneNormalize(login)
        }
        return map[_login]?.run {
            println("loginUser by: ${login}")
            //this.userInfo
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

    fun registerUserByPhone(
        fullName: String,
        phone: String
    ):User{
        return User.makeUser(fullName, phone = phone)
            .also { user-> map[user.login] = user
                println("User registered by phone: ${user.login} accessCode: ${user.accessCode}")
            }
    }

    fun requestAccessCode(phone: String) : String{
        return "000"
    }

    fun checkPhone(phone: String?):Boolean {
        val regex: String = "^\\+[^a-zA-Zа-яА-Я]+$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(phone)

        return !(!matcher.find() || phoneNormalize(
            phone
        ).length != 12)

            //throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")

    }

    private fun phoneNormalize(phone: String?) = phone?.replace("[^+\\d]".toRegex(),"").toString()
}