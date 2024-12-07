package _4.fortuna_bff.core

import io.konform.validation.ValidationResult

interface UseCase<in Request, out Response> {
	fun execute(request: Request): Response
}