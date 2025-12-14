package pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception;

public final class TeastoreErrorMessage {
    private TeastoreErrorMessage() {}

    public static final String INVARIANT_BREAK = "Aggregate %d breaks invariants";
    public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Invalid argument: %s";
    public static final String UNDEFINED_TRANSACTIONAL_MODEL = "Undefined transactional model";

    public static final String PRODUCT_DTO_NULL = "Product DTO cannot be null";
    public static final String PRODUCT_MISSING_NAME = "Product name is required";
    public static final String PRODUCT_INVALID_PRICE = "Product price must be non-negative";
    public static final String PRODUCT_INVALID_ID = "Product ID must be positive";

    public static final String USER_MISSING_USERNAME = "User username is required";
    public static final String USER_MISSING_EMAIL = "User email is required";
    public static final String USER_MISSING_REALNAME = "User real name is required";}