package com.dacn.Nhom8QLPhongTro.Validator;

import com.dacn.Nhom8QLPhongTro.Validator.annotation.ValidUserId;
import com.dacn.Nhom8QLPhongTro.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context){
        if(user==null)
            return true;
        return user.getId() != null;
    }

}
