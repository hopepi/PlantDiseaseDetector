package com.hope.tubitak_bitki.presentation.register

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() {
        val state = viewModel.state.value
        assertEquals("", state.name)
        assertEquals("", state.surname)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertEquals(null, state.error)
    }

    @Test
    fun `OnNameChange should update name in state`() {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        assertEquals("John", viewModel.state.value.name)
    }

    @Test
    fun `OnSurnameChange should update surname in state`() {
        viewModel.onEvent(RegisterEvent.OnSurnameChange("Doe"))
        assertEquals("Doe", viewModel.state.value.surname)
    }

    @Test
    fun `performRegister should return error when name is empty`() {
        viewModel.onEvent(RegisterEvent.OnRegisterClick)
        assertEquals("Ad alanı boş bırakılamaz.", viewModel.state.value.error)
    }

    @Test
    fun `performRegister should return error when surname is empty`() {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        viewModel.onEvent(RegisterEvent.OnRegisterClick)
        assertEquals("Soyad alanı boş bırakılamaz.", viewModel.state.value.error)
    }

    @Test
    fun `performRegister should return error when email is empty`() {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        viewModel.onEvent(RegisterEvent.OnSurnameChange("Doe"))
        viewModel.onEvent(RegisterEvent.OnRegisterClick)
        assertEquals("Email alanı boş bırakılamaz.", viewModel.state.value.error)
    }

    @Test
    fun `performRegister should return error when password is too short`() {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        viewModel.onEvent(RegisterEvent.OnSurnameChange("Doe"))
        viewModel.onEvent(RegisterEvent.OnEmailChange("john@example.com"))
        viewModel.onEvent(RegisterEvent.OnPasswordChange("123"))
        viewModel.onEvent(RegisterEvent.OnRegisterClick)
        assertEquals("Şifre en az 6 karakter olmalı.", viewModel.state.value.error)
    }

    @Test
    fun `performRegister should return error when passwords do not match`() {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        viewModel.onEvent(RegisterEvent.OnSurnameChange("Doe"))
        viewModel.onEvent(RegisterEvent.OnEmailChange("john@example.com"))
        viewModel.onEvent(RegisterEvent.OnPasswordChange("password123"))
        viewModel.onEvent(RegisterEvent.OnConfirmPasswordChange("password321"))
        viewModel.onEvent(RegisterEvent.OnRegisterClick)
        assertEquals("Şifreler eşleşmiyor!", viewModel.state.value.error)
    }

    @Test
    fun `performRegister should start loading and succeed when inputs are valid`() = runTest {
        viewModel.onEvent(RegisterEvent.OnNameChange("John"))
        viewModel.onEvent(RegisterEvent.OnSurnameChange("Doe"))
        viewModel.onEvent(RegisterEvent.OnEmailChange("john@example.com"))
        viewModel.onEvent(RegisterEvent.OnPasswordChange("password123"))
        viewModel.onEvent(RegisterEvent.OnConfirmPasswordChange("password123"))

        viewModel.onEvent(RegisterEvent.OnRegisterClick)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.state.value.isRegisterSuccessful)
        assertEquals(false, viewModel.state.value.isLoading)
    }
}
