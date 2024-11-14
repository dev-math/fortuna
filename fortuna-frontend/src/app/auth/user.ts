export class User {
  constructor(
    readonly name: string,
    readonly email: string,
    readonly roles: string[],
  ) {}

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.roles.includes(role))
  }
}
