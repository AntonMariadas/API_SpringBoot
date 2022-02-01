package com.projet.springapi.service;

import com.projet.springapi.dto.*;
import com.projet.springapi.entity.History;
import com.projet.springapi.entity.Job;
import com.projet.springapi.entity.User;
import com.projet.springapi.exception.*;
import com.projet.springapi.repository.HistoryRepository;
import com.projet.springapi.repository.JobRepository;
import com.projet.springapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<FavoriteJobDtoResponse> getUserFavoriteJobs(Long id) throws UserNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = requestedUser.get();
        return user.getFavoriteJobs()
                .stream()
                .map(this::favoriteJobDtoMapper)
                .collect(Collectors.toList());
    }

    public FavoriteJobDtoResponse getFavoriteJobById(Long id, Long jobId) throws JobNotFoundException, UserNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        Optional<Job> requestedJob = jobRepository.findById(jobId);
        if (requestedJob.isEmpty())
            throw new JobNotFoundException("JOB_WITH_ID_NOT_FOUND");

        Job job = requestedJob.get();
        return favoriteJobDtoMapper(job);
    }

    public FavoriteJobDtoResponse saveFavoriteJob(Long id, FavoriteJobDtoRequest request) throws UserNotFoundException, ExistingFavoriteJob {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = requestedUser.get();

        List<Job> favoriteJobs = user.getFavoriteJobs();
        for (Job job : favoriteJobs) {
            if ((job.getIdentifier()).equals(request.getIdentifier()))
                throw new ExistingFavoriteJob("JOB_ALREADY_IN_FAVORITES");
        }

        Job job = new Job(
                request.getName(),
                request.getIdentifier(),
                request.getField(),
                request.getProfile(),
                request.getSiret(),
                request.getWebsite(),
                request.getStars(),
                request.getAddress(),
                request.getAlternance(),
                request.getContactMode(),
                request.getSize(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAddedDate(),
                user
        );

        Job savedJob = jobRepository.save(job);
        return favoriteJobDtoMapper(savedJob);
    }

    public String deleteFavoriteJob(Long id, Long jobId) throws JobNotFoundException, UserNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        Optional<Job> requestedJob = jobRepository.findById(jobId);
        if (requestedJob.isEmpty())
            throw new JobNotFoundException("JOB_WITH_ID_NOT_FOUND");

        jobRepository.deleteById(jobId);
        return "JOB_WITH_ID " + jobId + "DELETED";
    }

    public FavoriteJobDtoResponse applyToFavoriteJob(Long id, Long jobId, ApplicationRequest request) throws UserNotFoundException, JobNotFoundException, InvalidDateException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        Optional<Job> requestedJob = jobRepository.findById(jobId);
        if (requestedJob.isEmpty())
            throw new JobNotFoundException("JOB_WITH_ID_NOT_FOUND");

        Job job = requestedJob.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String stringApplicationDate = request.getApplicationDate();
        LocalDate applicationDate = LocalDate.parse(stringApplicationDate, formatter);
        if (applicationDate.isAfter(LocalDate.now()))
            throw new InvalidDateException("application date should be a present or past date");

        LocalDate relaunchDate = applicationDate.plusWeeks(3);
        String stringRelaunchDate = relaunchDate.format(formatter);
        job.setApplicationDate(stringApplicationDate);
        job.setRelaunchDate(stringRelaunchDate);
        jobRepository.save(job);

        return favoriteJobDtoMapper(job);
    }

    public FavoriteJobDtoResponse unapplyFromFavoriteJob(Long id, Long jobId, ApplicationRequest request) throws UserNotFoundException, JobNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        Optional<Job> requestedJob = jobRepository.findById(jobId);
        if (requestedJob.isEmpty())
            throw new JobNotFoundException("JOB_WITH_ID_NOT_FOUND");

        Job job = requestedJob.get();
        job.setApplicationDate(request.getApplicationDate());
        job.setRelaunchDate(request.getApplicationDate());
        jobRepository.save(job);

        return favoriteJobDtoMapper(job);
    }

    public HistoryDtoResponse moveToHistory(Long id, HistoryDtoRequest request) throws UserNotFoundException, ExistingFavoriteJob {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = requestedUser.get();
        List<History> historyList = user.getApplicationHistory();
        for (History application : historyList) {
            if ((application.getIdentifier()).equals(request.getIdentifier()))
                throw new ExistingFavoriteJob("APPLICATION_ALREADY_IN_HISTORY");
        }

        History application = new History(
                request.getName(),
                request.getIdentifier(),
                request.getField(),
                request.getProfile(),
                request.getSiret(),
                request.getWebsite(),
                request.getAddress(),
                request.getApplicationDate(),
                request.getRelaunchDate(),
                user
        );
        History savedApplication = historyRepository.save(application);
        return historyDtoMapper(savedApplication);
    }

    public List<HistoryDtoResponse> getUserHistory(Long id) throws UserNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        User user = requestedUser.get();
        return user.getApplicationHistory()
                .stream()
                .map(this::historyDtoMapper)
                .collect(Collectors.toList());
    }

    public String deleteFromHistory(Long id, Long applicationId) throws UserNotFoundException, ApplicationInHistoryNotFoundException {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("USER_WITH_ID_NOT_FOUND");

        Optional<History> requestedApplication = historyRepository.findById(applicationId);
        if (requestedApplication.isEmpty())
            throw new ApplicationInHistoryNotFoundException("APPLICATION_WITH_ID_NOT_FOUND");

        historyRepository.deleteById(applicationId);
        return "APPLICATION_WITH_ID " + applicationId + "DELETED";
    }

    private FavoriteJobDtoResponse favoriteJobDtoMapper(Job job) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        FavoriteJobDtoResponse jobDto = new FavoriteJobDtoResponse();
        jobDto = modelMapper.map(job, FavoriteJobDtoResponse.class);
        return jobDto;
    }

    private HistoryDtoResponse historyDtoMapper(History application) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        HistoryDtoResponse applicationDto = new HistoryDtoResponse();
        applicationDto = modelMapper.map(application, HistoryDtoResponse.class);
        return applicationDto;
    }


}
