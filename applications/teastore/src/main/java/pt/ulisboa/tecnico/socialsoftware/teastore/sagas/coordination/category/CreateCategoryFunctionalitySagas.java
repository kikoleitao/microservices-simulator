package pt.ulisboa.tecnico.socialsoftware.teastore.sagas.coordination.category;

import pt.ulisboa.tecnico.socialsoftware.ms.coordination.workflow.WorkflowFunctionality;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaWorkflow;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.workflow.SagaSyncStep;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.service.CategoryService;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.CategoryDto;

public class CreateCategoryFunctionalitySagas extends WorkflowFunctionality {
    private CategoryDto createdCategoryDto;
    private final CategoryService categoryService;
    private final SagaUnitOfWorkService sagaUnitOfWorkService;

    public CreateCategoryFunctionalitySagas(CategoryService categoryService, SagaUnitOfWorkService sagaUnitOfWorkService,
                                            CategoryDto categoryDto, SagaUnitOfWork unitOfWork) {
        this.categoryService = categoryService;
        this.sagaUnitOfWorkService = sagaUnitOfWorkService;
        this.buildWorkflow(categoryDto, unitOfWork);
    }

    public void buildWorkflow(CategoryDto categoryDto, SagaUnitOfWork unitOfWork) {
        this.workflow = new SagaWorkflow(this, sagaUnitOfWorkService, unitOfWork);

        SagaSyncStep createCategoryStep = new SagaSyncStep("createCategoryStep", () -> {
            CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto, unitOfWork);
            setCreatedCategoryDto(createdCategoryDto);
        });

        workflow.addStep(createCategoryStep);
    }


    public CategoryDto getCreatedCategoryDto() {
        return createdCategoryDto;
    }

    public void setCreatedCategoryDto(CategoryDto createdCategoryDto) {
        this.createdCategoryDto = createdCategoryDto;
    }

}


