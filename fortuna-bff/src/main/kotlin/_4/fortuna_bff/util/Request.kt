package _4.fortuna_bff.util

import io.konform.validation.ValidationResult

interface Request<T> {
    fun validate(): ValidationResult<T>

    fun validateAndThrowOnError() {
        val errors = this.validate().errors
        if (errors.isNotEmpty()) {
            throw RuntimeException("Bad request")
        }
    }
}
