package ru.grineva.CourseProject.controller;


import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.grineva.CourseProject.dto.UserDto;
import ru.grineva.CourseProject.entity.Song;
import ru.grineva.CourseProject.entity.Author;
import ru.grineva.CourseProject.repository.SongsRepository;
import ru.grineva.CourseProject.repository.UserActionsRepository;
import ru.grineva.CourseProject.repository.UserRepository;
import ru.grineva.CourseProject.service.GetRoleService;
import ru.grineva.CourseProject.service.GetRoleServiceImp;
import ru.grineva.CourseProject.service.UserActionService;
import ru.grineva.CourseProject.service.UserService;

import java.util.List;
import java.util.Optional;
@Log4j2
@Slf4j
@Controller
public class SongsController {
    @Autowired
    private SongsRepository songsRepository;
    @Autowired
    private UserRepository userRepository;
    private GetRoleService getRoleServiceImp;
    private UserService userService;
    private UserActionService userActionService;

    public SongsController(GetRoleServiceImp getRoleServiceImp,
                           UserService userService,
                           UserActionService userActionService,
                           UserRepository userRepository)
    {
        this.getRoleServiceImp = getRoleServiceImp;
        this.userService = userService;
        this.userActionService = userActionService;
        this.userRepository = userRepository;
    }


    @Autowired
    private UserActionsRepository userActionsRepository;
    @GetMapping("/list")
    public ModelAndView getAllSongs() {
        ModelAndView mav = new ModelAndView("list-songs");
        UserDto userDto = userService.getCurrentUser();
        if (getRoleServiceImp.getRoleCurrentUser().equals("ROLE_ADMIN"))
        {
            mav.addObject("songs", songsRepository.findAll());
            mav.addObject("countOfSongs", songsRepository.findAll().size());
        }
        else
        {
            List<Song> songs = userRepository.findByEmail(userDto.getEmail()).getSongs();
            mav.addObject("songs", songs);
            mav.addObject("countOfSongs", userDto.getCountOfSongs());
        }
        mav.addObject("mainUserRole", userDto.getRole());
        log.info("/list -> connection");
        userActionService.setUserAction("connection to list-songs");
        return mav;
    }
    @GetMapping("/userActions")
    public ModelAndView getAllLogs() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("userActions", userActionsRepository.findAll());
        userActionService.setUserAction("connection to list-actions");
        return mav;
    }
    @GetMapping("/CalcSongNumber")
    public String calcSongNumber() {
        return "redirect:/list";
    }

    @GetMapping("/addSongForm")
    public ModelAndView addSongForm()
    {
        ModelAndView mav = new ModelAndView("add-song-form");
        Song song = new Song();
        mav.addObject("song", song);
        userActionService.setUserAction("connection to add-song-form");
        return mav;
    }
    @PostMapping("/saveSong")
    public String saveSong(@ModelAttribute Song song)
    {
        userActionService.setUserAction("save new song");
        Author user = userRepository.findByEmail(userService.getCurrentUser().getEmail());
        Optional<Song> optionalSong = songsRepository.findById((long) song.getId());
        if(optionalSong.isPresent())
        {
            user = optionalSong.get().getUser();
            Song song1 = user
                    .getSongs()
                    .stream()
                    .filter(x->x.getId() == song.getId())
                    .findFirst()
                    .get();
            song1.setName(song.getName());
            song1.setRevenue(song.getRevenue());
            userRepository.save(user);
            return "redirect:/list";
        }
        song.setAuthor(userService.getCurrentUser().getEmail());
        user.addSong(song);
        userRepository.save(user);
        return "redirect:/list";
    }
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long songId)
    {
        ModelAndView mav = new ModelAndView("add-song-form");
        Optional<Song> optionalSong = songsRepository.findById(songId);
        Song song = new Song();
        if(optionalSong.isPresent())
        {
            song = optionalSong.get();
        }
        mav.addObject("song", song);
        userActionService.setUserAction("modify song in database" + " by " + song.getAuthor());
        return mav;
    }
    @GetMapping("/deleteSong")
    public String deleteSong(@RequestParam Long songId)
    {
        Author user = userRepository.findById(songsRepository.findById(songId).get().getUser().getId()).get();
        Optional<Song> optionalSong = songsRepository.findById(songId);
        optionalSong.ifPresent(user::removeSong);
        optionalSong.ifPresent(songsRepository::delete);
        userActionService.setUserAction("delete song in database");
        return "redirect:/list";
    }

}
