package _4.fortuna_bff.core

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

interface UseCase<in Request, out Response> {
	fun execute(request: Request): Response
}

@Configuration
@ComponentScan(basePackages = ["_4.fortuna_bff"],
	includeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
		value = [UseCase::class])])
class UseCaseBeansConfig
