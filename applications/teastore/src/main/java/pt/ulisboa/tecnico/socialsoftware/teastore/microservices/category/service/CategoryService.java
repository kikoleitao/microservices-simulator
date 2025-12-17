package pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.aggregate.Category;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.aggregate.CategoryFactory;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.aggregate.CategoryRepository;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.CategoryDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreException;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private AggregateIdGeneratorService aggregateIdGeneratorService;

    private final CategoryRepository categoryRepository;

    private final UnitOfWorkService<UnitOfWork> unitOfWorkService;

    @Autowired
    private CategoryFactory categoryFactory;

    public CategoryService(UnitOfWorkService unitOfWorkService, CategoryRepository categoryRepository) {
        this.unitOfWorkService = unitOfWorkService;
        this.categoryRepository = categoryRepository;
    }

    @Retryable(
            value = { SQLException.class, CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryDto getCategoryById(Integer aggregateId, UnitOfWork unitOfWork) {
        if (!(aggregateId != null && aggregateId > 0)) {
            throw new TeastoreException("Aggregate id must be positive");
        }

        return categoryFactory.createCategoryDto((Category) unitOfWorkService.aggregateLoadAndRegisterRead(aggregateId, unitOfWork));
    }

    @Retryable(
            value = { SQLException.class, CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryDto createCategory(CategoryDto categoryDto, UnitOfWork unitOfWork) {
        if (categoryDto == null) {
            throw new TeastoreException("CategoryDto cannot be null");
        }

        Integer aggregateId = aggregateIdGeneratorService.getNewAggregateId();
        Category category = categoryFactory.createCategory(aggregateId, categoryDto);
        unitOfWorkService.registerChanged(category, unitOfWork);
        return categoryFactory.createCategoryDto(category);
    }

    @Retryable(
            value = { SQLException.class, CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryDto updateCategory(Integer aggregateId, CategoryDto categoryDto, UnitOfWork unitOfWork) {
        if (!(aggregateId != null && aggregateId > 0 && categoryDto != null)) {
            throw new TeastoreException("Invalid parameters for update");
        }

        Category oldCategory = (Category) unitOfWorkService.aggregateLoadAndRegisterRead(aggregateId, unitOfWork);
        if (oldCategory == null) {
            throw new TeastoreException("Category not found: " + aggregateId);
        }

        Category newCategory = categoryFactory.createCategoryFromExisting(oldCategory);
        if (categoryDto.getName() != null) newCategory.setName(categoryDto.getName());
        if (categoryDto.getDescription() != null) newCategory.setDescription(categoryDto.getDescription());

        unitOfWorkService.registerChanged(newCategory, unitOfWork);
        return categoryFactory.createCategoryDto(newCategory);
    }

    @Retryable(
            value = { SQLException.class, CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteCategory(Integer aggregateId, UnitOfWork unitOfWork) {
        if (!(aggregateId != null && aggregateId > 0)) {
            throw new TeastoreException("Aggregate id must be positive");
        }

        Category oldCategory = (Category) unitOfWorkService.aggregateLoadAndRegisterRead(aggregateId, unitOfWork);
        if (oldCategory == null) {
            throw new TeastoreException("Category not found: " + aggregateId);
        }

        Category newCategory = categoryFactory.createCategoryFromExisting(oldCategory);
        newCategory.remove();
        unitOfWorkService.registerChanged(newCategory, unitOfWork);
    }

    @Retryable(
            value = { SQLException.class, CannotAcquireLockException.class },
            maxAttemptsExpression = "${retry.db.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "${retry.db.delay}",
            multiplierExpression = "${retry.db.multiplier}"
        ))
    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryFactory::createCategoryDto)
                .collect(Collectors.toList());
    }

}
