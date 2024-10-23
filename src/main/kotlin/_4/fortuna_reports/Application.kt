package _4.fortuna_reports

import _4.fortuna_reports.utils.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@SpringBootApplication
@ComponentScan(basePackages = ["_4.fortuna_reports"],
	includeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
		value = [UseCase::class])])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}