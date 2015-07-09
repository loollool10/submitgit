package controllers

import javax.inject.Inject

import com.madgag.github.{PullRequestId, RepoId}
import lib._
import lib.model.PRMessageIdFinder.messageIdsByMostRecentUsageIn
import lib.model.{PRMessageIdFinder, MessageSummary}
import play.api.cache.Cached
import play.api.libs.json.Json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Api @Inject() (cached: Cached) extends Controller {

  def messageLookup(repoId: RepoId, query: String) = cached(s"$repoId $query") {
    Action.async {
      for {
        messagesOpt <- Project.byRepoId(repoId).mailingList.lookupMessage(query)
      } yield Ok(toJson(messagesOpt: Seq[MessageSummary]))
    }
  }

  def pullRequestMessages(prId: PullRequestId) = cached(s"$prId messages") {
    Action.async {
      val pr = Bot.conn().getRepository(prId.repo.fullName).getPullRequest(prId.num)
      val mailingList = Project.byRepoId(prId.repo).mailingList

      val messageIds = messageIdsByMostRecentUsageIn(pr)
      println(messageIds)
      for {
        messages <- Future.traverse(messageIds.take(3))(mailingList.lookupMessage)
      } yield Ok(toJson(messages.flatten: Seq[MessageSummary]))
    }
  }
}



