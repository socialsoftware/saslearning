package pt.ulisboa.tecnico.socialsoftware.saslearning.domain

/**
  * A Team represents a group of users that work together.
  *
  * Any [[User]] can create a team. The creator will be the team owner. Team owners
  * can add more owners or members. Owners are priviledge owners in a team. They can
  * add or remove members, add or remove owners, promote members to owners and demote
  * owners to members.
  *
  * @param name    the name of the team
  * @param owners  the owners of the team
  * @param members the other members of the team
  */
case class Team(name: NonEmptyString, owners: NonEmptySet[User], members: Set[User] = Set.empty) {

  def size: Int = owners.value.size + members.size

  def contains(user: User): Boolean = owners.value.contains(user) || members.contains(user)

  /**
    * Adds a new member to the team.
    *
    * It does nothing if the user is already a member of if it is already a owner.
    *
    * @param user the new member of the team.
    * @return the team corresponding to the update
    */
  def addMember(user: User): Team = if (contains(user)) {
    this
  } else {
    this.copy(members = members + user)
  }

  def removeMember(user: User): Team = this.copy(members = members - user)

  /**
    * Adds a new owner to the team.
    *
    * It does nothing if the user is already a owner. If the user is a member, it promotes
    * it to owner.
    *
    * @param user the new owner of the team.
    * @return the team corresponding to the update
    */
  def addOwner(user: User): Either[String, Team] = if (members.contains(user)) {
    promote(user)
  } else {
    updateOwners(owners.value + user)
  }

  def removeOwner(user: User): Either[String, Team] = updateOwners(owners.value - user)

  def promote(user: User): Either[String, Team] = removeMember(user).addOwner(user)

  def demote(user: User): Either[String, Team] = removeOwner(user).map(_.addMember(user))

  private def updateOwners(newOwners: Set[User]): Either[String, Team] =
    NonEmptySet(newOwners).map(o => this.copy(owners = o))
}

object Team {
  def fromUnsafe(name: String, owners: Set[User], members: Set[User] = Set.empty): Either[String, Team] = for {
    name <- NonEmptyString(name)
    owners <- NonEmptySet[User](owners)
  } yield new Team(name, owners, members)
}