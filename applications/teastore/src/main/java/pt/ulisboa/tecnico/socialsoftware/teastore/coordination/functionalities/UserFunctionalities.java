package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities;

import static pt.ulisboa.tecnico.socialsoftware.ms.TransactionalModel.SAGAS;
import static pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreErrorMessage.*;

import java.util.Arrays;

import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import pt.ulisboa.tecnico.socialsoftware.ms.TransactionalModel;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.teastore.sagas.coordination.user.*;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.service.UserService;

@Service
public class UserFunctionalities {
    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private SagaUnitOfWorkService sagaUnitOfWorkService;

    @Autowired
    private Environment env;

    private TransactionalModel workflowType;

    @PostConstruct
    public void init() {
        String[] activeProfiles = env.getActiveProfiles();
        if (Arrays.asList(activeProfiles).contains(SAGAS.getValue())) {
            workflowType = SAGAS;
        } else {
            throw new TeastoreException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public UserDto createUser(UserDto userDto) throws TeastoreException {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                checkInput(userDto);

                CreateUserFunctionalitySagas createUserFunctionalitySagas = new CreateUserFunctionalitySagas(
                        userService, sagaUnitOfWorkService, userDto, sagaUnitOfWork);

                createUserFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                return createUserFunctionalitySagas.getCreatedUserDto();
            default: throw new TeastoreException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public UserDto findByUserId(Integer userAggregateId) {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                FindByUserIdFunctionalitySagas findByUserIdFunctionalitySagas = new FindByUserIdFunctionalitySagas(
                        userService, sagaUnitOfWorkService, userAggregateId, sagaUnitOfWork);
                findByUserIdFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                return findByUserIdFunctionalitySagas.getUserDto();
            default: throw new TeastoreException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public void deleteUser(Integer userAggregateId) throws TeastoreException {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                DeleteUserFunctionalitySagas deleteUserFunctionalitySagas = new DeleteUserFunctionalitySagas(
                        userService, sagaUnitOfWorkService, sagaUnitOfWork);
                deleteUserFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                break;
            default: throw new TeastoreException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    private void checkInput(UserDto userDto) {
        if (userDto.getUserName() == null) {
            throw new TeastoreException(TeastoreErrorMessage.USER_MISSING_USERNAME);
        }
        if (userDto.getEmail() == null) {
            throw new TeastoreException(USER_MISSING_EMAIL);
        }
        if (userDto.getRealName() == null) {
            throw new TeastoreException(TeastoreErrorMessage.USER_MISSING_REALNAME);
        }
    }

}