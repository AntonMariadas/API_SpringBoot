package com.projet.springapi.serviceTest;

import com.projet.springapi.dto.AccountDtoRequest;
import com.projet.springapi.dto.AccountDtoResponse;
import com.projet.springapi.entity.Address;
import com.projet.springapi.entity.Role;
import com.projet.springapi.entity.User;
import com.projet.springapi.exception.ExistingEmailException;
import com.projet.springapi.exception.InvalidEmailException;
import com.projet.springapi.exception.UserNotFoundException;
import com.projet.springapi.repository.UserRepository;
import com.projet.springapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AccountService accountService;

    private final User user = new User(
            "Anton",
            "Mariadas",
            "test@gmail.com",
            "12345678",
            "developpement",
            Role.USER,
            new Address(
                    42,
                    "chemin de la grille",
                    "93330",
                    "Neuilly sur Marne",
                    23.45,
                    45.678
            )
    );
    private AccountDtoRequest account = new AccountDtoRequest(
            "Anton",
            "Mariadas",
            "test@gmail.com",
            "12345678",
            "developpement",
            42,
            "chemin de la grille",
            "93330",
            "Neuilly sur Marne",
            23.45,
            45.678
    );
    private final AccountDtoResponse response = new AccountDtoResponse(
            2L,
            "Anton",
            "Mariadas",
            "test@gmail.com",
            "developpement",
            "USER",
            42,
            "chemin de la grille",
            "93330",
            "Neuilly sur Marne",
            23.45,
            45.678
    );

    @Test
    void canGetAllUsers() {
        // when
        accountService.getAllUsers();
        //then
        verify(userRepository).findAll();
    }

    @Test
    void canGetUserById() throws UserNotFoundException {
        //given
        Long id = 2L;
        given(userRepository.findById(id)).willReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> accountService.getUserById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("USER_WITH_ID_NOT_FOUND");
    }

    @Test
    void canGetUserByEmail() throws UserNotFoundException {
        //given
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> accountService.getUserByEmail(user.getEmail()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("USER_WITH_EMAIL_NOT_FOUND");
    }

    @Test
    void canSaveUser() throws InvalidEmailException, ExistingEmailException {
        //given
        //when
        //then
    }

    @Test
    void shouldUpdateUser() {
        //given
        //when
        //then
    }

    @Test
    void shouldDeleteUser() {
        //given
        //when
        //then
    }

    @Test
    void shouldUpdateRoleToAdmin() {
        //given
        //when
        //then
    }

    @Test
    void shouldUpdateRoleToUser() {
        //given
        //when
        //then
    }
}
