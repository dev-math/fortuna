package _4.fortuna_reports.utils

interface UseCase<in Request, out Response> {
    fun execute(request: Request): Response
}
