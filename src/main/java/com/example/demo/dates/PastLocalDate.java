package com.example.demo.dates;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Date;

//此类保证提交上来的日期皆为过去的值，而非未来
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastLocalDate.PastValidator.class)
@Documented
public @interface PastLocalDate {
    String message() default "{javax.validation.constraints.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PastValidator implements ConstraintValidator<PastLocalDate,Date>{
        public void initialize(PastLocalDate past){}

        @Override
        public boolean isValid(Date date, ConstraintValidatorContext context) {
            return date==null || date.before(new Date());

        }
    }

}
