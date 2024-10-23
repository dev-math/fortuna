package _4.fortuna_reports.authorization

@Service
class AuthorizationService(
	private val studentRepository: StudentRepository,
) {
	fun getLoggedUser(): OidcUser? {
		val authentication = SecurityContextHolder.getContext().authentication
		return authentication.principal as? OidcUser
	}
	companion object {
		private val logger: Logger = LoggerFactory.getLogger(AuthorizationService::class.java)
	}
}
