package ru.grineva.CourseProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.grineva.CourseProject.entity.UserActions;
import ru.grineva.CourseProject.repository.UserActionsRepository;
import ru.grineva.CourseProject.util.DateTimeUtil;

import java.util.Date;

@Service
public class UserActionServiceImp implements UserActionService{

    private UserActionsRepository userActionsRepository;
    private UserService userService;
    @Autowired
    public void UserActionsServiceImp(UserActionsRepository userActionsRepository,
                                      UserService userService)
    {
        this.userActionsRepository = userActionsRepository;
        this.userService = userService;
    }
    @Override
    public void setUserAction(String userAction) {
        String date = DateTimeUtil.getCustomFormat().format(new Date());
        UserActions userActions = new UserActions();
        userActions.setDate_actions(date);
        userActions.setUser_email(userService.getCurrentUser().getEmail());
        userActions.setDescription(userAction);
        userActionsRepository.save(userActions);
    }
}
