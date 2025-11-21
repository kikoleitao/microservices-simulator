package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities;

import static pt.ulisboa.tecnico.socialsoftware.ms.TransactionalModel.SAGAS;
import static pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreErrorMessage.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.TeastoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import pt.ulisboa.tecnico.socialsoftware.ms.TransactionalModel;
import pt.ulisboa.tecnico.socialsoftware.ms.coordination.unitOfWork.UnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWork;
import pt.ulisboa.tecnico.socialsoftware.ms.sagas.unitOfWork.SagaUnitOfWorkService;
import pt.ulisboa.tecnico.socialsoftware.teastore.sagas.coordination.order.*;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.order.service.OrderService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.service.UserService;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.orderfactory.service.OrderFactory;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.OrderDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.OrderUserDto;

@Service
public class OrderFunctionalities {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private SagaUnitOfWorkService sagaUnitOfWorkService;

    @Autowired
    private OrderFactory orderFactory;


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

    public OrderDto createOrder(OrderDto orderDto) throws TeastoreException {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                CreateOrderFunctionalitySagas createOrderFunctionalitySagas = new CreateOrderFunctionalitySagas(
                        orderService, sagaUnitOfWorkService, sagaUnitOfWork);
                createOrderFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                return createOrderFunctionalitySagas.getCreatedOrder();
            default: throw new AnswersException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public OrderDto findByOrderId(Integer orderAggregateId) {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                FindByOrderIdFunctionalitySagas findByOrderIdFunctionalitySagas = new FindByOrderIdFunctionalitySagas(
                        orderService, sagaUnitOfWorkService, sagaUnitOfWork);
                findByOrderIdFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                return findByOrderIdFunctionalitySagas.getResult();
            default: throw new AnswersException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public List<OrderDto> findByUserAggregateId(Integer userAggregateId) {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                FindByUserAggregateIdFunctionalitySagas findByUserAggregateIdFunctionalitySagas = new FindByUserAggregateIdFunctionalitySagas(
                        orderService, sagaUnitOfWorkService, sagaUnitOfWork);
                findByUserAggregateIdFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                return findByUserAggregateIdFunctionalitySagas.getFindByUserAggregateId();
            default: throw new AnswersException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

    public void cancelOrder(Integer orderAggregateId) throws TeastoreException {
        String functionalityName = new Throwable().getStackTrace()[0].getMethodName();

        switch (workflowType) {
            case SAGAS:
                SagaUnitOfWork sagaUnitOfWork = sagaUnitOfWorkService.createUnitOfWork(functionalityName);
                CancelOrderFunctionalitySagas cancelOrderFunctionalitySagas = new CancelOrderFunctionalitySagas(
                        orderService, sagaUnitOfWorkService, sagaUnitOfWork);
                cancelOrderFunctionalitySagas.executeWorkflow(sagaUnitOfWork);
                break;
            default: throw new AnswersException(UNDEFINED_TRANSACTIONAL_MODEL);
        }
    }

}