package com.s1511.ticketcine.application.implementations;

import com.s1511.ticketcine.domain.entities.FunctionDetails;
import com.s1511.ticketcine.domain.entities.Screen;
import com.s1511.ticketcine.domain.repository.ScreenRepository;
import com.s1511.ticketcine.domain.services.MovieService;
import com.s1511.ticketcine.domain.services.SeatService;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.s1511.ticketcine.application.dto.movie.ReadDtoMovie;
import com.s1511.ticketcine.application.mapper.MovieMapper;
import com.s1511.ticketcine.domain.repository.FunctionDetailsRepository;
import com.s1511.ticketcine.domain.repository.MovieRepository;
import com.s1511.ticketcine.domain.services.FunctionDetailsService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionDetailsServiceImpl implements FunctionDetailsService {

    private final FunctionDetailsRepository functionDetailsRepositoryRepository;
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final ScreenRepository screenRepository;
    private final SeatService seatService;

    @Override
    public ReadDtoMovie getMovieById(String idMovie) {
        return movieMapper.movieToReadDto(movieRepository.findByIdAndActive(idMovie,true).orElse(null));
    }

    @Override
    @Transactional
    public void createFunctionsForScreen(String screenId) {
        var screens = screenRepository.findAll();
        LocalDateTime time = LocalDateTime.now().plusDays(14);
        for (Screen screen: screens) {
            for (int i = 0; i < 3; i++) {
                FunctionDetails functionDetail = new FunctionDetails();
                functionDetail.setScreenId(screenId);
                if (i == 0) {
                    functionDetail.setSchedule(time.with(LocalTime.of(18,0)));
                } else if (i == 1) {
                    functionDetail.setSchedule(time.with(LocalTime.of(21,0)));
                } else if (i == 2) {
                    functionDetail.setSchedule(time.plusDays(1).with(LocalTime.of(0, 0)));
                }
                functionDetail.setSeatsList(seatService.createSeatMatrix(functionDetail.getId()));
                functionDetail.setMovieId(movieService.getRandomMovieId());
                functionDetail.setActive(true);
            }
        }

    }

}
