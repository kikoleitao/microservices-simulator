package pt.ulisboa.tecnico.socialsoftware.teastore.sagas.coordination.user;

import pt.ulisboa.tecnico.socialsoftware.ms.coordination.workflow.WorkflowFunctionality;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaWorkflow;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaSyncStep;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.service.UserService;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;

public class FindByUserIdFunctionalitySagas extends WorkflowFunctionality {
    private UserDto userDto;
    private final UserService userService;
    private final SagaUnitOfWorkService sagaUnitOfWorkService;

    public FindByUserIdFunctionalitySagas(UserService userService, SagaUnitOfWorkService sagaUnitOfWorkService,
                                          Integer userAggregateId, SagaUnitOfWork unitOfWork){
        this.userService = userService;
        this.sagaUnitOfWorkService = sagaUnitOfWorkService;

        this.buildWorkflow(userAggregateId, unitOfWork);

    }

    public void buildWorkflow(Integer userAggregateId, SagaUnitOfWork unitOfWork) {
        this.workflow = new SagaWorkflow(this, sagaUnitOfWorkService, unitOfWork);

        SagaSyncStep findUserStep = new SagaSyncStep("findUserStep", () -> {
            UserDto userDto = userService.getUserById(userAggregateId, unitOfWork);
            this.setUserDto(userDto);
        });

        workflow.addStep(findUserStep);
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}


