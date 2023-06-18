package ru.clevertec.newsservice.util;

import io.envoyproxy.pgv.ReflectiveValidatorIndex;
import io.envoyproxy.pgv.ValidationException;
import io.envoyproxy.pgv.ValidatorIndex;
import lombok.experimental.UtilityClass;
import ru.clevertec.exceptionhandlerstarter.exception.ProtoValidationException;

/**
 * A utility class for validating protobuf objects using the Protobuf Java API. This class uses a
 * {@link ReflectiveValidatorIndex} to validate the given object and throws a {@link ProtoValidationException}
 * if validation fails.
 */
@UtilityClass
public class ProtobufValidator {

    /**
     * Validates the given protobuf object using the Protobuf Java API.
     *
     * @param object the object to validate.
     * @throws ProtoValidationException if validation fails.
     */
    public void validateProto(Object object) {
        ValidatorIndex index = new ReflectiveValidatorIndex();
        try {
            index.validatorFor(object.getClass()).assertValid(object);
        } catch (ValidationException e) {
            throw new ProtoValidationException(e.getMessage());
        }
    }

}
