package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록하기")
    fun saveBookTest() {
        //given
        val bookName = "이상한 나라의 엘리스"
        val request = BookRequest(bookName)

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo(bookName)
    }

    @Test
    @DisplayName("책 빌리기")
    fun loanBookTest() {
        //given
        val bookName = "이상한 나라의 엘리스"
        bookRepository.save(Book(bookName))
        val savedUser = userRepository.save(User("홍길동", null))
        val request = BookLoanRequest("홍길동", bookName)

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo(bookName)
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].isReturn).isFalse
    }

    @Test
    @DisplayName("책이 대출되어 있으면 신규 대출은 실패함")
    fun loanBookFailTest() {
        //given
        val bookName = "이상한 나라의 엘리스"
        bookRepository.save(Book(bookName))
        val savedUser = userRepository.save(User("홍길동", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, bookName, false))
        val request = BookLoanRequest("홍길동", bookName)

        //when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납하기")
    fun returnBookTest() {
        //given
        val bookName = "이상한 나라의 엘리스"
        bookRepository.save(Book(bookName))
        val savedUser = userRepository.save(User("홍길동", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, bookName, false))
        val request = BookReturnRequest("홍길동", bookName)

        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].isReturn).isTrue
    }
}