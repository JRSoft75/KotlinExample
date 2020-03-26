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
    ): User {
        return User.makeUser(fullName, email = email, password = password)
            .also { user ->
                map[user.login] = user
                println("User registered by login: ${user.login}")
            }
    }

    fun loginUser(login: String, password: String): String? {
        //return map.toString()
        var _login = ""
        if (checkPhone(login) == false) {
            _login = login.trim()
        } else {
            //login is phone
            _login =
                phoneNormalize(login)
        }
        return map[_login]?.run {
            println("loginUser by: ${login}")
            //this.userInfo
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }

    fun registerUserByPhone(
        fullName: String,
        phone: String
    ): User {
        return User.makeUser(fullName, phone = phone)
            .also { user ->
                map[user.login] = user
                println("User registered by phone: ${user.login} accessCode: ${user.accessCode}")
            }
    }

    /*Реализуй метод requestAccessCode(login: String) : Unit, после выполнения данного метода
    у пользователя с соответствующим логином должен быть сгенерирован новый код авторизации
    и помещен в свойство accessCode, соответственно должен измениться и хеш пароля пользователя
    (вызов метода loginUser должен отрабатывать корректно)*/
    fun requestAccessCode(login: String): Unit {
        var _login =  phoneNormalize(login)

        map[_login]?.run {
            changeAccessCode(login)
            map[this.login] = this
        }

    }



    fun checkPhone(phone: String?): Boolean {
        val regex: String = "^\\+[^a-zA-Zа-яА-Я]+$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(phone)

        return !(!matcher.find() || phoneNormalize(
            phone
        ).length != 12)

        //throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")

    }

    private fun phoneNormalize(phone: String?) = phone?.replace("[^+\\d]".toRegex(), "").toString()
}