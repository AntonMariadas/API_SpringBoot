package com.projet.springapi.controller;

import com.projet.springapi.dto.*;
import com.projet.springapi.exception.*;
import com.projet.springapi.service.AccountService;
import com.projet.springapi.service.JobService;
import com.projet.springapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://frosty-allen-630331.netlify.app")
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDtoResponse>> getAllUsers() {
        List<AccountDtoResponse> usersDto = accountService.getAllUsers();
        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDtoResponse> getOneUser(@PathVariable Long id) throws UserNotFoundException {
        AccountDtoResponse userDto = accountService.getUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountDtoResponse> editUser(@PathVariable Long id, @Valid @RequestBody AccountDtoRequest request) throws UserNotFoundException {
        AccountDtoResponse userDto = accountService.updateUser(id, request);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Long id) throws UserNotFoundException {
        String response = accountService.deleteUser(id);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<AccountDtoResponse> signUp(@Valid @RequestBody AccountDtoRequest request) throws ExistingEmailException, InvalidEmailException {
        AccountDtoResponse userDto = accountService.saveUser(request);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/accounts/login")
    public ResponseEntity<AuthenticationResponse> signIn(@Valid @RequestBody AuthenticationRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        }
        catch (Exception e) {
            throw new InvalidCredentialsException("WRONG_EMAIL_OR_PASSWORD");
        }
        String jwtToken = jwtUtil.generateToken(authRequest.getEmail());

        return new ResponseEntity<>(new AuthenticationResponse(jwtToken), HttpStatus.OK);
    }

    @GetMapping("/accounts/user")
    public ResponseEntity<AccountDtoResponse> getOneByEmail(@RequestParam String email) throws UserNotFoundException {
        AccountDtoResponse userDto = accountService.getUserByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}/upgrade")
    public ResponseEntity<AccountDtoResponse> upgradeUser(@PathVariable Long id) throws UserNotFoundException, AlreadySetRoleException {
        AccountDtoResponse userDto = accountService.updateRoleToAdmin(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}/downgrade")
    public ResponseEntity<AccountDtoResponse> downgradeUser(@PathVariable Long id) throws UserNotFoundException, AlreadySetRoleException {
        AccountDtoResponse userDto = accountService.updateRoleToUser(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/accounts/{id}/jobs")
    public ResponseEntity<FavoriteJobDtoResponse> addJobToFavorites(@PathVariable Long id, @Valid @RequestBody FavoriteJobDtoRequest request) throws UserNotFoundException, ExistingFavoriteJob {
        FavoriteJobDtoResponse jobDto = jobService.saveFavoriteJob(id, request);
        return new ResponseEntity<>(jobDto, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}/jobs")
    public ResponseEntity<List<FavoriteJobDtoResponse>> getFavoriteJobs(@PathVariable Long id) throws UserNotFoundException {
        List<FavoriteJobDtoResponse> jobsDto = jobService.getUserFavoriteJobs(id);
        return new ResponseEntity<>(jobsDto, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}/jobs/{jobId}")
    public ResponseEntity<FavoriteJobDtoResponse> getOneFavoriteJob(@PathVariable Long id, @PathVariable Long jobId) throws UserNotFoundException, JobNotFoundException {
        FavoriteJobDtoResponse jobDto = jobService.getFavoriteJobById(id, jobId);
        return new ResponseEntity<>(jobDto, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}/jobs/{jobId}")
    public ResponseEntity<FavoriteJobDtoResponse> applyToFavoriteJob(@PathVariable Long id, @PathVariable Long jobId, @Valid @RequestBody ApplicationRequest request) throws UserNotFoundException, JobNotFoundException, InvalidDateException {
        FavoriteJobDtoResponse jobDto = jobService.applyToFavoriteJob(id, jobId, request);
        return new ResponseEntity<>(jobDto, HttpStatus.OK);
    }

    @PutMapping("/accounts/{id}/jobs/{jobId}/unapply")
    public ResponseEntity<FavoriteJobDtoResponse> unapplyFromFavoriteJob(@PathVariable Long id, @PathVariable Long jobId, @Valid @RequestBody ApplicationRequest request) throws UserNotFoundException, JobNotFoundException {
        FavoriteJobDtoResponse jobDto = jobService.unapplyFromFavoriteJob(id, jobId, request);
        return new ResponseEntity<>(jobDto, HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{id}/jobs/{jobId}")
    public ResponseEntity<String> removeJobFromFavorites(@PathVariable Long id, @PathVariable Long jobId) throws UserNotFoundException, JobNotFoundException {
        String response = jobService.deleteFavoriteJob(id, jobId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/accounts/{id}/history")
    public ResponseEntity<HistoryDtoResponse> moveJobToHistory(@PathVariable Long id, @Valid @RequestBody HistoryDtoRequest request) throws UserNotFoundException, ExistingFavoriteJob {
        HistoryDtoResponse historyDto = jobService.moveToHistory(id, request);
        return new ResponseEntity<>(historyDto, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}/history")
    public ResponseEntity<List<HistoryDtoResponse>> getUserHistory(@PathVariable long id) throws UserNotFoundException {
        List<HistoryDtoResponse> historyListDto = jobService.getUserHistory(id);
        return new ResponseEntity<>(historyListDto, HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{id}/history/{applicationId}")
    public ResponseEntity<String> removeFromHistory(@PathVariable Long id,@PathVariable Long applicationId) throws UserNotFoundException, ApplicationInHistoryNotFoundException {
        String response = jobService.deleteFromHistory(id, applicationId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
