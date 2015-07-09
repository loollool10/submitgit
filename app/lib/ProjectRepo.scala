package lib

import com.madgag.github.RepoId
import lib.model.MessageSummary

import scala.concurrent.{ExecutionContext, Future}

case class MailingList(emailAddress: String, archives: Seq[MailArchive]) {
  def lookupMessage(query: String)(implicit ec: ExecutionContext): Future[Seq[MessageSummary]] =
    Future.find(archives.map(_.lookupMessage(query)))(_.nonEmpty).map(_.toSeq.flatten)
}

object Project {
  val Git = Project(
    RepoId("git", "git"),
    MailingList("git@vger.kernel.org", Seq(
      Gmane.Git,
      Marc.Git
      // MailArchiveDotCom("git@vger.kernel.org") -- message-id search appears broken
    ))
  )
  val PretendGit = Project(
    RepoId("submitgit", "pretend-git"),
    GoogleGroup("submitgit-test").mailingList
  )

  val all = Set(Git, PretendGit)

  val byRepoId = all.map(p => p.repoId -> p).toMap
}

case class Project(repoId: RepoId, mailingList: MailingList)
