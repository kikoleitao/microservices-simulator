package pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.socialsoftware.ms.coordination.unitOfWork.UnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.coordination.unitOfWork.UnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.ms.domain.aggregate.AggregateIdGeneratorService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.aggregate.User;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.aggregate.UserFactory;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.aggregate.UserRepository;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;

@Service
public class UserService {

    @Autowired
    private AggregateIdGeneratorService aggregateIdGeneratorService;

    private final UserRepository userRepository;

    private final UnitOfWorkService<UnitOfWork> unitOfWorkService;

    @Autowired
    private UserFactory userFactory;

    public UserService(UnitOfWorkService unitOfWorkService, UserRepository userRepository) {
        this.unitOfWorkService = unitOfWorkService;
        this.userRepository = userRepository;
    }

    @Retryable(
            value = { SQLException.class,  CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto getUserById(Integer aggregateId, UnitOfWork unitOfWork) {
        return userFactory.createUserDto((User) unitOfWorkService.aggregateLoadAndRegisterRead(aggregateId, unitOfWork));
    }

    /* simple user creation */
    @Retryable(
            value = { SQLException.class,  CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createUser(UserDto userDto, UnitOfWork unitOfWork) {
        Integer aggregateId = aggregateIdGeneratorService.getNewAggregateId();
        User user = userFactory.createUser(aggregateId, userDto);
        unitOfWorkService.registerChanged(user, unitOfWork);
        return userFactory.createUserDto(user);
    }

    @Retryable(
            value = { SQLException.class,  CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "${retry.db.delay}",
                    multiplierExpression = "${retry.db.multiplier}"
            ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(Integer userAggregateId, UnitOfWork unitOfWork) {
        User oldUser = (User) unitOfWorkService.aggregateLoadAndRegisterRead(userAggregateId, unitOfWork);
        User newUser = userFactory.createUserFromExisting(oldUser);
        newUser.remove();
        unitOfWorkService.registerChanged(newUser, unitOfWork);
    }
}
