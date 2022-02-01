package com.projet.springapi.service;

import com.projet.springapi.dto.*;
import com.projet.springapi.entity.*;
import com.projet.springapi.exception.*;
import com.projet.springapi.repository.UserRepository;
import com.projet.springapi.util.EmailValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    public List<AccountDtoResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::accountDtoMapper)
                .collect(Collectors.toList());
    }

    public AccountDtoResponse getUserById(Long id) throws UserNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = requestedUser.get();
        return accountDtoMapper(user);
    }

    public AccountDtoResponse getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> requestedUser = userRepository.findByEmail(email);
        if(requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_EMAIL_NOT_FOUND");

        User user = requestedUser.get();
        return accountDtoMapper(user);
    }

    public AccountDtoResponse saveUser(AccountDtoRequest request) throws ExistingEmailException, InvalidEmailException {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail)
            throw new InvalidEmailException("EMAIL_NOT_VALID");

        boolean isUserExists = userRepository.findByEmail(request.getEmail()).isPresent();
        if (isUserExists)
            throw new ExistingEmailException("EMAIL_ALREADY_TAKEN");

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        Address address = new Address(
                request.getNumber(),
                request.getRoad(),
                request.getCity(),
                request.getPostCode(),
                request.getLatitude(),
                request.getLongitude()
        );
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                encodedPassword,
                request.getProfile(),
                Role.USER,
                address
        );

        User savedUser = userRepository.save(user);
        return accountDtoMapper(savedUser);
    }

    public AccountDtoResponse updateUser(Long id, AccountDtoRequest request) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = existingUser.get();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encodedPassword);
        user.setProfile(request.getProfile());
        user.getAddress().setNumber(request.getNumber());
        user.getAddress().setRoad(request.getRoad());
        user.getAddress().setCity(request.getCity());
        user.getAddress().setPostCode(request.getPostCode());
        user.getAddress().setLatitude(request.getLatitude());
        user.getAddress().setLongitude(request.getLongitude());

        User updatedUser = userRepository.save(user);
        return accountDtoMapper(updatedUser);
    }

    public String deleteUser(Long id) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        userRepository.deleteById(id);
        return "USER_WITH_ID " + id + "DELETED";
    }

    public AccountDtoResponse updateRoleToAdmin(Long id) throws UserNotFoundException, AlreadySetRoleException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = existingUser.get();
        if (user.getRole() == Role.ADMIN)
            throw new AlreadySetRoleException("USER_ROLE_IS_ALREADY_ADMIN");

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        return accountDtoMapper(user);
    }

    public AccountDtoResponse updateRoleToUser(Long id) throws UserNotFoundException, AlreadySetRoleException {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = existingUser.get();
        if (user.getRole() == Role.USER)
            throw new AlreadySetRoleException("USER_ROLE_IS_ALREADY_USER");

        user.setRole(Role.USER);
        userRepository.save(user);

        return accountDtoMapper(user);
    }

    private AccountDtoResponse accountDtoMapper(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        AccountDtoResponse accountDto = new AccountDtoResponse();
        accountDto = modelMapper.map(user, AccountDtoResponse.class);
        return accountDto;
    }
}
