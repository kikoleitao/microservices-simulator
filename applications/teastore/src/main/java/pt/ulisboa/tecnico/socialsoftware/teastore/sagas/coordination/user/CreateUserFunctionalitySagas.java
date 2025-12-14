package pt.ulisboa.tecnico.socialsoftware.teastore.sagas.coordination.user;

import pt.ulisboa.tecnico.socialsoftware.ms.coordination.workflow.WorkflowFunctionality;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaSyncStep;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaWorkflow;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.service.UserService;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;

public class CreateUserFunctionalitySagas extends WorkflowFunctionality {
    private UserDto createdUserDto;
    private final UserService userService;
    private final SagaUnitOfWorkService sagaUnitOfWorkService;

    public CreateUserFunctionalitySagas(UserService userService, SagaUnitOfWorkService sagaUnitOfWorkService,
                                        UserDto userDto, SagaUnitOfWork unitOfWork) {
        this.userService = userService;
        this.sagaUnitOfWorkService = sagaUnitOfWorkService;
        this.buildWorkflow(userDto, unitOfWork);
    }

    public void buildWorkflow(UserDto userDto, SagaUnitOfWork unitOfWork) {
        this.workflow = new SagaWorkflow(this, sagaUnitOfWorkService, unitOfWork);

        SagaSyncStep createUserStep = new SagaSyncStep("createUserStep", () -> {
            UserDto createdUserDto = userService.createUser(userDto, unitOfWork);
            setCreatedUserDto(createdUserDto);
        });

        workflow.addStep(createUserStep);
    }

    public UserDto getCreatedUserDto() {
        return createdUserDto;
    }

    public void setCreatedUserDto(UserDto createdUserDto) {
        this.createdUserDto = createdUserDto;
    }

}
