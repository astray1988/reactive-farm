package controllers

import javax.inject.Inject

import models.Vocabulary
import play.api.i18n.Lang
import play.api.mvc.{ Action, Controller }
import services.VocabularService

/**
 * Created by dylan on 2/13/16.
 */
class Import @Inject() (vocabulary: VocabularService) extends Controller {
  def importWord(sourceLanguage: Lang, targetLanguage: Lang, word: String, translation: String) = Action { implicit request =>
    val added = vocabulary.addVocabulary(Vocabulary(sourceLanguage, targetLanguage, word, translation))
    if (added)
      Ok
    else
      Conflict // return a 409 Conflict response
  }

}
