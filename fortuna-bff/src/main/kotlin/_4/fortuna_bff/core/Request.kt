package _4.fortuna_bff.core

import io.konform.validation.ValidationResult

interface Request<R> {
    fun validate(request: R): ValidationResult<R>
}