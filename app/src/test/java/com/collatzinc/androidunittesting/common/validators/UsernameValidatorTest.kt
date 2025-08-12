package com.collatzinc.androidunittesting.common.validators

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class UsernameValidatorTest(
    private val input: String,
    private val isOptional: Boolean,
    private val expectedResult: Boolean
) {

    private val validator: UserNameValidator = UserNameValidator()

    @Test
    fun test (){
        val result = validator.validate(input,isOptional = isOptional)
        assertEquals(result.isValid,expectedResult)
    }

    companion object Companion {

        @JvmStatic
        @Parameterized.Parameters(name = "Case {index} : input=\"{0}\", isOptional={1} => expectedResult={2}")
        fun data():List<Array<Any?>>{
            return  listOf(
                arrayOf("",false,false),
                arrayOf("  ",false,false),
                arrayOf("",true,true),
                arrayOf("username",false,true),
                arrayOf("username",true,true),
            )
        }
    }

}