package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows

@DisplayName("계산기 테스트")
class JunitCalculatorTest {


    @Test
    //@Order(1)
    @DisplayName("더하기 테스트")
    fun addTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.add(3)

        //then
        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    //@Order(2)
    @DisplayName("빼기 테스트")
    fun minusTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.minus(3)

        //then
        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    //@Order(3)
    @DisplayName("곱하기 테스트")
    fun multiplyTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.multiply(3)

        //then
        assertThat(calculator.number).isEqualTo(15)
    }

    @Test
    //@Order(4)
    @DisplayName("나누기 테스트")
    fun divideTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.divide(2)

        //then
        assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    //@Order(5)
    @DisplayName("예외 테스트")
    fun divideExceptionTest() {
        //given
        val calculator = Calculator(5)

        //when & then
        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply {
            assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
        }
    }
}