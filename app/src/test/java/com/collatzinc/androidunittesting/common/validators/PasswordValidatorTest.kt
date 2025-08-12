package com.collatzinc.androidunittesting.common.validators

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class PasswordValidatorTest(
    private val input: String,
    private val isOptional: Boolean,
    private val expectedResult: Boolean
) {

    private val validator: PasswordValidator = PasswordValidator()

    @Test
    fun test (){
        val result = validator.validate(input,isOptional = isOptional)
        assertEquals(result.isValid,expectedResult)
    }

    companion object{

        @JvmStatic
        @Parameterized.Parameters(name = "Case {index} : input=\"{0}\", isOptional={1} => expectedResult={2}")
        fun data():List<Array<Any?>>{
            return  listOf(
                arrayOf("",false,false),
                arrayOf("  ",false,false),
                arrayOf("",true,true),
                arrayOf("ValidPassword",false,true),
                arrayOf("ValidPassword",true,true),
                arrayOf(" StartingWithSpace",false,false),
                arrayOf("EndingWithSpace ",false,false),
                arrayOf("12345678",false,true),
                arrayOf("123456",false,false),
                arrayOf("Invalid",false,false),
            )
        }
    }

}