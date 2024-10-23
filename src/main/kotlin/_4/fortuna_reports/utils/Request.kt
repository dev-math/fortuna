package _4.fortuna_reports.utils

import _4.fortuna_reports.entitymanager.usecases.RegisterUserRequest
import io.konform.validation.ValidationResult

interface Request<T> {
    fun validate(): ValidationResult<T>
}

data class ValidationResult(val isValid: Boolean, val errors: List<String> = emptyList())